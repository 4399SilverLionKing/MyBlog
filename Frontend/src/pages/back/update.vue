<script setup lang="ts">
import type { BlogApiInterface } from '~/apis/blogApi'
import { ElMessage } from 'element-plus'
import { onMounted, reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useBlogStore } from '~/stores/blogStore'
import 'vditor/dist/index.css'
import '~/styles/vditor.css'

// 获取路由对象
const route = useRoute()
const router = useRouter()
const blogId = ref<number | null>(null)
const isCreate = ref(true)

// 使用博客store
const blogStore = useBlogStore()

// 博客系统背景图片
const background = '/images/BlackSea.png'

// Markdown编辑器实例
const vditor = ref<any>(null)
const vditorEl = ref<HTMLElement | null>(null)
const loading = ref(false)

// 博客表单数据
const blogForm = reactive<{
  title: string
  desc: string
  category: string
  tags: string
  status: string
  content: string
}>({
  title: '',
  desc: '',
  category: 'code',
  tags: '',
  status: '',
  content: '',
})

// 分类选项
const categories = [
  { label: '代码', value: 'code' },
  { label: '阅读', value: 'read' },
  { label: '生活', value: 'life' },
]

// 在组件加载时初始化编辑器并检查是否为编辑模式
onMounted(async () => {
  const id = route.query.id ? Number(route.query.id) : null
  blogId.value = id
  isCreate.value = !id

  // 延迟初始化编辑器，确保DOM已渲染
  setTimeout(() => {
    initEditor()
  }, 100)

  // 如果是编辑模式，加载博客数据
  if (id) {
    await loadBlogData(id)
  }
})

// 初始化Vditor编辑器
async function initEditor() {
  if (!vditorEl.value)
    return

  // 动态导入Vditor
  const Vditor = (await import('vditor')).default

  // 创建编辑器实例
  vditor.value = new Vditor(vditorEl.value, {
    height: '100%',
    mode: 'ir',
    theme: 'dark',
    toolbar: [
      'emoji',
      'headings',
      'bold',
      'italic',
      'strike',
      'link',
      '|',
      'list',
      'ordered-list',
      'check',
      'outdent',
      'indent',
      '|',
      'quote',
      'line',
      'code',
      'inline-code',
      'insert-before',
      'insert-after',
      '|',
      'upload',
      'table',
      '|',
      'undo',
      'redo',
      '|',
      'fullscreen',
      'preview',
    ],
    placeholder: '请输入博客内容...',
    preview: {
      theme: {
        current: 'dark',
      },
    },
    cache: {
      enable: false,
    },
    upload: {
      accept: 'image/*',
      handler: async (_files: File[]) => {
        // 这里可以实现上传图片的逻辑
        ElMessage.warning('图片上传功能暂未实现')
        return 'error'
      },
    },
    after: () => {
      // 设置初始内容
      if (blogForm.content) {
        vditor.value.setValue(blogForm.content)
      }
    },
    input: (value: string) => {
      blogForm.content = value
      // 如果摘要为空，自动截取内容前100个字符作为摘要
      if (!blogForm.desc?.trim()) {
        const text = value.replace(/[#*`>]/g, '').trim()
        blogForm.desc = text.substring(0, 100)
      }
    },
  })
}

// 加载博客数据
async function loadBlogData(id: number) {
  loading.value = true
  try {
    // 从博客存储中获取博客数据
    const blog = blogStore.getBlogById(id)
    if (!blog) {
      ElMessage.error('博客不存在')
      router.push('/back/manage')
      return
    }

    // 填充表单
    blogForm.title = blog.title
    blogForm.desc = blog.desc
    blogForm.category = blog.category
    blogForm.tags = blog.tags?.join(', ') || ''

    // 注意：Blog类型中没有content属性，这里暂时使用空字符串
    // 实际项目中可能需要通过API单独获取博客内容
    blogForm.content = ''

    // 如果编辑器已初始化，设置内容
    if (vditor.value) {
      vditor.value.setValue(blogForm.content)
    }
  }
  catch (error) {
    console.error('加载博客失败:', error)
    ElMessage.error(`加载失败: ${(error as Error).message}`)
  }
  finally {
    loading.value = false
  }
}

// 保存博客
async function saveBlog() {
  if (!blogForm.title.trim()) {
    ElMessage.error('博客标题不能为空')
    return
  }

  if (!blogForm.content.trim()) {
    ElMessage.error('博客内容不能为空')
    return
  }

  // 处理标签
  const tagsList = blogForm.tags
    ? blogForm.tags.split(',').map(tag => tag.trim()).filter(Boolean)
    : []

  const blogData: BlogApiInterface = {
    title: blogForm.title,
    content: blogForm.content,
    desc: blogForm.desc,
    category: blogForm.category,
    tags: tagsList,
    status: blogForm.status,
  }

  loading.value = true
  try {
    if (isCreate.value) {
      // 创建新博客
      await blogStore.addBlog(blogData)
      ElMessage.success('博客创建成功')
    }
    else if (blogId.value) {
      // 更新现有博客
      await blogStore.updateBlog(blogData)
      ElMessage.success('博客更新成功')
    }

    // 返回管理页面
    router.push('/back/manage')
  }
  catch (error) {
    console.error('保存博客失败:', error)
    ElMessage.error(`保存失败: ${(error as Error).message}`)
  }
  finally {
    loading.value = false
  }
}

// 返回管理页面
function goBack() {
  router.push('/back/manage')
}
</script>

<template>
  <!-- 主容器，全屏显示并使用背景图片 -->
  <div class="h-screen w-screen relative overflow-hidden">
    <!-- 背景图片 -->
    <img :src="background" alt="背景" class="h-full w-full inset-0 absolute object-cover">

    <!-- 半透明遮罩层 -->
    <div class="bg-dark-800/70 inset-0 absolute" />

    <!-- 内容区域 -->
    <div class="p-4 flex h-full w-full items-center justify-center relative">
      <div class="border border-gray-900 rounded-3xl flex flex-col h-full w-full shadow-xl overflow-hidden backdrop-blur-sm md:flex-row">
        <!-- 右侧主内容区域 -->
        <div class="p-4 flex flex-1 flex-col overflow-auto md:p-8">
          <!-- 博客表单 -->
          <div v-if="loading" class="py-12 text-center flex flex-1 flex-col items-center justify-center">
            <div class="i-carbon-circle-dash text-6xl text-teal-400/50 mx-auto mb-4 animate-spin" />
            <p class="text-light-400">
              加载中...
            </p>
          </div>

          <div v-else class="p-5 border border-white/10 rounded-xl flex flex-1 flex-col shadow-lg backdrop-blur-sm">
            <!-- 基本信息 -->
            <div class="mb-5 gap-4 grid grid-cols-1 lg:grid-cols-4 md:grid-cols-2">
              <!-- 标题 -->
              <div class="mb-2">
                <input
                  v-model="blogForm.title"
                  type="text"
                  placeholder="博客标题"
                  class="text-sm text-light-200 px-4 py-2 border border-white/10 rounded-lg bg-dark-800/70 w-full focus:outline-unset focus:ring-1 focus:ring-teal-500"
                >
              </div>

              <!-- 分类 -->
              <div>
                <select
                  v-model="blogForm.category"
                  class="text-sm text-light-200 px-4 py-2 border border-white/10 rounded-lg bg-dark-800/70 w-full focus:outline-unset focus:ring-1 focus:ring-teal-500"
                >
                  <option v-for="category in categories" :key="category.value" :value="category.value">
                    {{ category.label }}
                  </option>
                </select>
              </div>

              <!-- 标签 -->
              <div>
                <input
                  v-model="blogForm.tags"
                  type="text"
                  placeholder="标签1, 标签2, 标签3"
                  class="text-sm text-light-200 px-4 py-2 border border-white/10 rounded-lg bg-dark-800/70 w-full focus:outline-unset focus:ring-1 focus:ring-teal-500"
                >
              </div>

              <!-- 状态 -->
              <div>
                <select
                  v-model="blogForm.status"
                  class="text-sm text-light-200 px-4 py-2 border border-white/10 rounded-lg bg-dark-800/70 w-full focus:outline-unset focus:ring-1 focus:ring-teal-500"
                >
                  <option :value="0">
                    草稿
                  </option>
                  <option :value="1">
                    发布
                  </option>
                </select>
              </div>
            </div>

            <!-- 摘要 -->
            <div class="mb-5">
              <textarea
                v-model="blogForm.desc"
                placeholder="博客摘要"
                rows="2"
                class="text-sm text-light-200 p-3 border border-white/10 rounded-lg bg-dark-800/70 w-full focus:outline-unset focus:ring-1 focus:ring-teal-500"
              />
            </div>

            <!-- Markdown编辑器 -->
            <div class="flex flex-1 flex-col min-h-[500px]">
              <div ref="vditorEl" class="vditor-container flex-1 h-full" />
            </div>

            <!-- 提交按钮 -->
            <div class="mt-5 flex justify-end">
              <button
                class="text-white mr-3 px-6 py-2 rounded-lg bg-gray-600/80 flex gap-2 transition-colors items-center backdrop-blur-sm hover:bg-gray-500"
                @click="goBack"
              >
                <span>取消</span>
              </button>
              <button
                class="text-white px-6 py-2 rounded-lg bg-teal-600/80 flex gap-2 transition-colors items-center backdrop-blur-sm hover:bg-teal-500"
                :disabled="loading"
                @click="saveBlog"
              >
                <div v-if="loading" class="i-carbon-circle-dash animate-spin" />
                <div v-else class="i-carbon-save" />
                <span>{{ isCreate ? '创建' : '保存' }}</span>
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
