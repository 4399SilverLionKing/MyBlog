<script setup lang='ts'>
import SideBar from '@/components/SideBar.vue'

// 背景图片
const background = '/images/XIn.png'

// 导航项
const navItems = [
  { icon: 'i-carbon-home', label: 'home', goto: '/front/home' },
  { icon: 'i-carbon-bookmark', label: 'blog', goto: '/front/blogs' },
  { icon: 'i-carbon-user', label: 'aboutMe', goto: '/front/about' },
]
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
            关于作者
          </h1>
          <div class="px-6 py-8 border border-white/10 rounded-xl bg-dark-700/50 backdrop-blur-sm">
            <p class="text-light-300 mb-4">
              这里是关于作者的介绍...
            </p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
