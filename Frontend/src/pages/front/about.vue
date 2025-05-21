<script setup lang='ts'>
import SideBar from '@/components/SideBar.vue'
import axios from 'axios'
import Vditor from 'vditor'
import { nextTick, onMounted, ref } from 'vue'
import 'vditor/dist/index.css'
import '~/styles/vditor.css'

// 背景图片
const background = '/images/XIn.png'

// 导航项
const navItems = [
  { icon: 'i-carbon-home', label: 'home', goto: '/front/home' },
  { icon: 'i-carbon-bookmark', label: 'blog', goto: '/front/blogs' },
  { icon: 'i-carbon-user', label: 'aboutMe', goto: '/front/about' },
]

// Markdown内容
const markdownContent = ref('')
const isLoading = ref(true)

// 预览元素
let previewElement: HTMLDivElement | null = null

// 加载Markdown文件
async function loadMarkdownFile() {
  isLoading.value = true
  try {
    // 从public目录加载about.md文件
    const response = await axios.get('/about.md')
    markdownContent.value = response.data
  }
  catch (error) {
    console.error('加载Markdown文件失败:', error)
    // 设置默认内容，以防文件不存在
    markdownContent.value = ``
  }
  finally {
    isLoading.value = false
    renderMarkdown()
  }
}

// 渲染Markdown内容
function renderMarkdown() {
  nextTick(() => {
    setTimeout(() => {
      previewElement = document.getElementById('vditor-preview') as HTMLDivElement
      if (previewElement) {
        try {
          Vditor.preview(previewElement, markdownContent.value, {
            mode: 'dark', // 使用暗色模式
            markdown: {
              toc: true,
              linkBase: '',
              paragraphBeginningSpace: true,
            },
            hljs: {
              style: 'monokai', // 代码高亮主题
              lineNumber: true,
              enable: true, // 确保启用代码高亮
            },
            after: () => {
              // Markdown渲染完成后的回调
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
    }, 300)
  })
}

onMounted(() => {
  loadMarkdownFile()
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
          <h1 class="text-3xl text-white font-bold mb-6">
            关于我的一切
          </h1>
          <!-- 加载中状态 -->
          <div v-if="isLoading" class="py-12 flex items-center justify-center">
            <div class="i-carbon-circle-dash text-3xl animate-spin" />
          </div>
          <!-- 内容区域 -->
          <div v-else class="px-6 py-8 border border-white/10 rounded-xl bg-dark-700/50 backdrop-blur-sm">
            <!-- 使用vditor预览元素显示Markdown内容 -->
            <div
              id="vditor-preview"
              class="vditor-reset prose prose-invert text-left max-w-full"
            />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
