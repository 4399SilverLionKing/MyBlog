import type { Blog, BlogListParams } from '~/apis/blogApi'
import { defineStore } from 'pinia'
import { computed, ref } from 'vue'
import {
  createBlog,
  deleteBlog,
  getBlogList,
  putBlog,
} from '~/apis/blogApi'

// 定义博客Store
export const useBlogStore = defineStore('blog', () => {
  // 状态
  const blogs = ref<Blog[]>([])
  const currentBlog = ref<Blog | null>(null)
  const activeCategory = ref('all')
  const searchQuery = ref('')
  const loading = ref(false)
  const totalCount = ref(0)
  const currentPage = ref(1)
  const pageSize = ref(10)

  // Getters
  const filteredBlogs = computed(() => {
    return blogs.value.filter((blog) => {
      const matchCategory = activeCategory.value === 'all' || blog.category === activeCategory.value
      const matchSearch = blog.title?.toLowerCase().includes(searchQuery.value.toLowerCase()) || true
      return matchCategory && matchSearch
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

  function setCurrentBlog(blog: Blog | null) {
    currentBlog.value = blog
  }

  // 获取博客列表
  async function fetchBlogs(params?: Partial<BlogListParams>) {
    loading.value = true
    try {
      const apiParams: BlogListParams = {
        page: params?.page || currentPage.value,
        pageSize: params?.pageSize || pageSize.value,
        keyword: params?.keyword || searchQuery.value,
        category: params?.category || (activeCategory.value === 'all' ? undefined : activeCategory.value),
      }

      const res = await getBlogList(apiParams)
      if (res.code === 200) {
        blogs.value = res.data.list.map(item => ({
          id: item.id || 0,
          title: item.title || '',
          desc: item.desc || '',
          category: item.category || 'code',
          date: item.date?.split('T')[0] || new Date().toISOString().split('T')[0],
          readCount: item.readCount || 0,
          tags: item.tags || [],
        }))
        currentPage.value = apiParams.page
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
  async function addBlog(blogData: Omit<Blog, 'id'>) {
    loading.value = true
    try {
      const res = await createBlog(blogData)
      if (res.code === 200) {
        // 成功后重新获取博客列表
        await fetchBlogs()
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
  async function updateBlog(blogData: Partial<Blog>) {
    loading.value = true
    try {
      const res = await putBlog(blogData as Blog)
      if (res.code === 200) {
        // 成功后重新获取博客列表
        await fetchBlogs()
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
      if (res.code === 200) {
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
  }
})
