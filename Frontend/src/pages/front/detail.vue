<script setup lang='ts'>
import Vditor from 'vditor'
import { computed, nextTick, onMounted, ref } from 'vue'
import SideBar from '~/components/SideBar.vue'
import { useBlogStore } from '~/stores/blogStore'
import 'vditor/dist/index.css'
import '~/styles/vditor.css'

// 使用博客store
const blogStore = useBlogStore()

// 背景图片
const background = '/images/XIn.png'

// 从store获取当前博客
const currentBlog = computed(() => blogStore.currentBlog)

// 博客数据
const blogTitle = computed(() => currentBlog.value?.title || '加载中...')
const blogDate = computed(() => currentBlog.value?.date || '')
const blogReadCount = computed(() => currentBlog.value?.readCount)
const isLoading = ref(true)
const blogContent = ref('')

// 导航项
const navItems = [
  { icon: 'i-carbon-home', label: 'Home', goto: '/front/home' },
  { icon: 'i-carbon-bookmark', label: 'Blogs', goto: '/front/blogs' },
  { icon: 'i-carbon-user', label: 'About Me', goto: '/front/about' },
]

// 预览元素
let previewElement: HTMLDivElement | null = null

// 从七牛云获取博客内容
async function loadBlogDetail() {
  isLoading.value = true
  try {
    // 检查store中是否有当前博客和ID
    if (currentBlog.value && currentBlog.value.id) {
      // 增加阅读量
      await blogStore.updateReadCount(currentBlog.value.id)

      try {
        // 使用store方法获取博客内容
        const contentResult = await blogStore.getBlogContent(currentBlog.value.id)

        if (contentResult.success) {
          blogContent.value = contentResult.content
        }
        else {
          console.error('获取博客内容失败')
          blogContent.value = ''
        }
      }
      catch (error) {
        console.error('获取博客内容失败:', error)
        blogContent.value = ''
      }
    }
    else {
      console.warn('未找到当前博客数据')
      blogContent.value = ''
    }

    // 使用nextTick确保DOM已更新后再渲染Markdown
    nextTick(() => {
      setTimeout(() => {
        previewElement = document.getElementById('vditor-preview') as HTMLDivElement
        if (previewElement) {
          console.warn('找到预览元素，准备渲染Markdown')
          try {
            Vditor.preview(previewElement, blogContent.value, {
              mode: 'dark', // 使用暗色模式
              markdown: {
                toc: true,
                linkBase: '',
                paragraphBeginningSpace: true, // 段落开始时保留空格，有助于保持左对齐
              },
              hljs: {
                style: 'github', // 代码高亮主题
                lineNumber: true,
                enable: true, // 确保启用代码高亮
              },
              after: () => {
                // Markdown渲染完成后的回调
                console.warn('Markdown渲染完成')
                // 添加额外的CSS类来强制左对齐
                if (previewElement) {
                  const codeBlocks = previewElement.querySelectorAll('pre code')
                  codeBlocks.forEach((block) => {
                    block.classList.add('text-left')
                    // 确保每个代码块添加hljs类
                    if (!block.classList.contains('hljs')) {
                      block.classList.add('hljs')
                    }
                  })
                }
              },
            })
          }
          catch (err) {
            console.error('Vditor预览错误:', err)
          }
        }
        else {
          console.error('未找到预览元素，ID: vditor-preview')
        }
      }, 300) // 增加延时时间
    })
  }
  catch (error) {
    console.error('博客内容加载失败', error)
  }
  finally {
    isLoading.value = false
  }
}

onMounted(() => {
  console.warn('组件挂载完成，开始加载博客')
  // 组件挂载后再加载数据
  loadBlogDetail()
})
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
        <!-- 使用SideBar组件 -->
        <SideBar :nav-items="navItems" />

        <!-- 右侧主内容区域 -->
        <div class="p-4 flex flex-1 flex-col overflow-auto md:p-8">
          <!-- 顶部标题区域 -->
          <div class="mb-8 flex flex-col">
            <h1 class="text-3xl text-white font-bold mb-2 md:text-4xl">
              {{ blogTitle }}
            </h1>
            <div class="flex gap-3">
              <span class="flex items-center">
                <div class="i-carbon-calendar mr-1.5 opacity-70" />
                {{ blogDate }}
              </span>
              <span class="flex items-center">
                <div class="i-carbon-view mr-1.5 opacity-70" />
                {{ blogReadCount }}
              </span>
            </div>
          </div>

          <!-- 博客内容区域 -->
          <div v-if="isLoading" class="flex flex-grow items-center justify-center">
            <div class="text-xl text-light-300 animate-pulse">
              加载中...
            </div>
          </div>
          <div v-else class="p-6 border border-white/10 rounded-xl bg-dark-700/50 backdrop-blur-sm">
            <div
              id="vditor-preview"
              class="vditor-reset prose prose-invert text-left max-w-full"
            />
          </div>

          <!-- 底部区域 -->
          <div class="mt-6 flex items-center justify-between">
            <div class="flex gap-2">
              <button class="text-light-300 border border-white/10 rounded-full flex h-10 w-10 items-center justify-center hover:bg-dark-700/70">
                <span class="i-carbon-favorite" />
              </button>
              <button class="text-light-300 border border-white/10 rounded-full flex h-10 w-10 items-center justify-center hover:bg-dark-700/70">
                <span class="i-carbon-share" />
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
