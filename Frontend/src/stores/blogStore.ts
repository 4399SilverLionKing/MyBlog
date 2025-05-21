import type { BlogApiInterface, BlogListParams } from '~/apis/blogApi'
import axios from 'axios'
import { defineStore } from 'pinia'
import { computed, ref } from 'vue'
import {
  createBlog,
  deleteBlog,
  getBlogList,
  putBlog,
  updateBlogReadCount,
} from '~/apis/blogApi'
import { getSignedDownloadUrl, postBlogContent, updateBlogContent } from '~/apis/qiniuApi'

export interface BlogList {
  id: number
  title: string
  subtitle: string
  category: string
  date: string
  readCount: number
  tags: string[]
  status: string // 0: 草稿, 1: 已发布
}
// 定义博客Store
export const useBlogStore = defineStore('blog', () => {
  // 状态
  const blogs = ref<BlogList[]>([])
  const currentBlog = ref<BlogList | null>(null)
  const activeCategory = ref('all')
  const searchQuery = ref('')
  const loading = ref(false)
  const totalCount = ref(0)
  const currentPage = ref(1)
  const pageSize = ref(10)

  // Getters
  const filteredBlogs = computed(() => {
    return blogs.value.filter((blog) => {
      // 首先获取已按分类筛选的博客
      // 分类匹配逻辑
      const matchCategory = activeCategory.value === 'all'
        || blog.category === activeCategory.value

      // 搜索匹配逻辑：匹配标题、副标题或标签
      const query = searchQuery.value.toLowerCase()
      const matchSearch = query === ''
        || blog.title.toLowerCase().includes(query)
        || blog.subtitle.toLowerCase().includes(query)
        || (blog.tags && blog.tags.some(tag => tag.toLowerCase().includes(query)))

      return matchCategory && matchSearch
    }).sort((a, b) => {
      // 按修改时间（日期）降序排序，最新的排在前面
      const dateA = new Date(a.date).getTime()
      const dateB = new Date(b.date).getTime()
      return dateB - dateA
    })
  })

  // Actions
  function setCategory(category: string) {
    activeCategory.value = category
  }

  function setSearchQuery(query: string) {
    searchQuery.value = query
  }

  function getBlogById(id: number) {
    return blogs.value.find(blog => blog.id === id) || null
  }

  function setCurrentBlog(blog: BlogList | null) {
    currentBlog.value = blog
  }

  // 获取博客列表
  async function fetchBlogs(params?: Partial<BlogListParams>) {
    loading.value = true
    try {
      const apiParams: BlogListParams = {
        pageIndex: params?.pageIndex || currentPage.value,
        pageSize: params?.pageSize || pageSize.value,
        keyword: params?.keyword || searchQuery.value,
        category: params?.category || (activeCategory.value === 'all' ? undefined : activeCategory.value),
      }

      const res = await getBlogList(apiParams)
      if (res.code === 10000) {
        blogs.value = res.data.rows.map(item => ({
          id: item.id || 0,
          title: item.title || '',
          subtitle: item.subtitle || '',
          category: item.category?.toLowerCase() || 'code',
          date: item.date?.split('T')[0] || new Date().toISOString().split('T')[0],
          readCount: item.readCount || 0,
          tags: item.tags || [],
          status: item.status || '',
        }))
        currentPage.value = apiParams.pageIndex
        pageSize.value = apiParams.pageSize
      }
      return res
    }
    catch (error) {
      console.error('获取博客列表失败:', error)
      throw error
    }
    finally {
      loading.value = false
    }
  }

  // 添加博客
  async function addBlog(blogData: BlogApiInterface, blogContent: string) {
    loading.value = true
    try {
      const res = await createBlog(blogData)
      if (res.code === 10000) {
        // 成功后重新获取博客列表
        await fetchBlogs()
        if (res.data.id && res.data.token && blogContent) {
          // 上传博客内容到七牛云
          await postBlogContent(res.data.id, blogContent, res.data.token)
        }
      }
      return res
    }
    catch (error) {
      console.error('添加博客失败:', error)
      throw error
    }
    finally {
      loading.value = false
    }
  }

  // 更新博客
  async function updateBlog(blogData: BlogApiInterface, blogContent: string) {
    loading.value = true
    try {
      const res = await putBlog(blogData)
      if (res.code === 10000) {
        // 成功后重新获取博客列表
        await fetchBlogs()
        console.warn('开始上传')
        // 如果有内容，上传到七牛云
        if (res.data && blogData.id && blogContent) {
          await updateBlogContent(blogData.id, blogContent, res.data)
        }
      }
      return res
    }
    catch (error) {
      console.error('更新博客失败:', error)
      throw error
    }
    finally {
      loading.value = false
    }
  }

  // 删除博客
  async function removeBlog(id: number) {
    loading.value = true
    try {
      const res = await deleteBlog(id)
      if (res.code === 10000) {
        // 成功后重新获取博客列表
        await fetchBlogs()
      }
      return res
    }
    catch (error) {
      console.error('删除博客失败:', error)
      throw error
    }
    finally {
      loading.value = false
    }
  }

  // 从七牛云获取博客内容
  async function getBlogContent(id: number) {
    try {
      // 获取博客的key
      const blogKey = `${id}.md`

      // 获取七牛云带签名的下载地址
      const signedUrlResponse = await getSignedDownloadUrl(blogKey)

      if (signedUrlResponse.code === 10000 && signedUrlResponse.data.url) {
        // 使用获取到的签名URL下载博客内容
        const contentResponse = await axios.get(signedUrlResponse.data.url)
        return {
          success: true,
          content: contentResponse.data,
        }
      }
      else {
        console.error('获取七牛云下载链接失败')
        return {
          success: false,
          content: '',
          message: '获取博客内容失败，请检查网络连接',
        }
      }
    }
    catch (error) {
      console.error('获取博客内容失败:', error)
      return {
        success: false,
        content: '',
        message: '获取博客内容失败，请检查网络连接',
      }
    }
  }

  // 增加博客阅读量
  async function updateReadCount(id: number) {
    try {
      const blog = blogs.value.find(b => b.id === id)
      if (!blog)
        return

      // 使用新的公共接口更新阅读量
      const res = await updateBlogReadCount(id)
      if (res.code === 10000) {
        // 更新本地博客列表中的阅读量
        if (blog) {
          blog.readCount = (blog.readCount || 0) + 1
        }
      }
      return res
    }
    catch (error) {
      console.error('更新阅读量失败:', error)
      throw error
    }
  }

  return {
    blogs,
    currentBlog,
    activeCategory,
    searchQuery,
    loading,
    totalCount,
    currentPage,
    pageSize,
    filteredBlogs,
    setCategory,
    setSearchQuery,
    getBlogById,
    setCurrentBlog,
    fetchBlogs,
    addBlog,
    updateBlog,
    removeBlog,
    getBlogContent,
    updateReadCount,
  }
})
