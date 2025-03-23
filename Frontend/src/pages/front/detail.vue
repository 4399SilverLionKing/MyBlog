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
const blogReadCount = computed(() => currentBlog.value?.readCount || '')
const isLoading = ref(true)
const blogContent = ref('')

// 导航项
const navItems = [
  { icon: 'i-carbon-home', label: 'Home', goto: '/front/home' },
  { icon: 'i-carbon-bookmark', label: 'Blogs', goto: '/front/blogs' },
  { icon: 'i-carbon-user', label: 'About Me', goto: '/front/about' },
]

// 这里直接提供一个示例Markdown内容
// 不再使用模板字符串，直接使用实际Markdown内容
const sampleMarkdown = `# Vue3最佳实践指南

## 前言

Vue3 是 Vue.js 的最新主要版本，它带来了许多激动人心的新特性和性能改进。本文将带你了解使用 Vue3 的最佳实践。

## 组合式API (Composition API)

组合式API是Vue3最核心的特性之一，它允许我们按照逻辑关注点组织代码。

### 基本使用

\`\`\`javascript
import { ref, computed, onMounted } from 'vue'

export default {
  setup() {
    // 响应式状态
    const count = ref(0)

    // 计算属性
    const doubleCount = computed(() => count.value * 2)

    // 方法
    function increment() {
      count.value++
    }

    // 生命周期钩子
    onMounted(() => {
      console.log('Component mounted!')
    })

    // 暴露给模板
    return {
      count,
      doubleCount,
      increment
    }
  }
}
\`\`\`

### script setup语法糖

\`\`\`vue


<template>
  <button @click="increment">Count: {{ count }}</button>
  <p>Double count: {{ doubleCount }}</p>
</template>
\`\`\`

## 性能优化

### 1. 使用\`v-memo\`缓存模板部分

\`\`\`vue
<template>
  <div v-memo="[item.id, selected]">
    <!-- 只有当item.id或selected变化时才会更新这部分模板 -->
  </div>
</template>
\`\`\`

### 2. 异步组件和Suspense

\`\`\`javascript
import { defineAsyncComponent } from 'vue'

const AsyncComponent = defineAsyncComponent(() =>
  import('./components/AsyncComponent.vue')
)
\`\`\`

## 总结

Vue3的组合式API、性能优化和TypeScript支持使得开发大型应用程序更加高效和可维护。积极采用这些最佳实践，将会极大提升您的开发体验。

希望本文对您有所帮助！`

// Vditor实例
let previewElement: HTMLDivElement | null = null

// 加载博客内容
function loadBlogDetail() {
  isLoading.value = true
  try {
    // 检查store中是否有当前博客
    if (currentBlog.value) {
      // 直接使用store中的数据
      blogContent.value = sampleMarkdown
    }
    else {
      console.warn('未找到当前博客数据，使用示例内容')
      blogContent.value = sampleMarkdown
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
                style: 'monokai', // 代码高亮主题
                lineNumber: true,
              },
              after: () => {
                // Markdown渲染完成后的回调
                console.warn('Markdown渲染完成')
                // 添加额外的CSS类来强制左对齐
                if (previewElement) {
                  const codeBlocks = previewElement.querySelectorAll('pre code')
                  codeBlocks.forEach((block) => {
                    block.classList.add('text-left')
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
