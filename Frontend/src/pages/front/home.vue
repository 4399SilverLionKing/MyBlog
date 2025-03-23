<script setup lang="ts">
import type { BlogList } from '~/stores/blogStore'
import { computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import BlogCard from '~/components/BlogCard.vue'
import SideBar from '~/components/SideBar.vue'
import { useBlogStore } from '~/stores/blogStore'

// 路由器实例
const router = useRouter()

// 使用博客store
const blogStore = useBlogStore()

// 定义博客数据
const blogTitle = 'Sanyeyeye'
const background = '/images/XIn.png'

const navItems = [
  { icon: 'i-carbon-home', label: 'Home', goto: '/front/home' },
  { icon: 'i-carbon-bookmark', label: 'Blogs', goto: '/front/blogs' },
  { icon: 'i-carbon-user', label: 'About Me', goto: '/front/about' },
  { icon: 'i-carbon-settings', label: 'Settings', goto: '/back/data', isSettings: true },
]

// 获取置顶博客（拥有"置顶"标签的博客）
const pinnedBlogs = computed(() => {
  return blogStore.blogs
    .filter((blog: BlogList) => blog.tags?.includes('置顶'))
})

// 获取最新博客（根据日期排序，取前4篇）
const recentBlogs = computed(() => {
  return [...blogStore.blogs]
    .sort((a: BlogList, b: BlogList) => new Date(b.date).getTime() - new Date(a.date).getTime())
    .slice(0, 4) // 最新的4篇博客
})

// 跳转到博客详情页
function navigateToBlog(blogId: number) {
  // 设置当前博客并导航到详情页
  const blog = blogStore.getBlogById(blogId)
  if (blog) {
    blogStore.setCurrentBlog(blog)
    router.push('/front/detail')
  }
}

// 页面加载时获取博客列表
onMounted(() => {
  blogStore.fetchBlogs()
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
          <div class="mb-8 text-center md:text-left">
            <h1 class="text-3xl text-white font-bold mb-2 md:text-4xl">
              {{ blogTitle }}
            </h1>
          </div>

          <!-- 内容区域 -->
          <div class="space-y-8">
            <!-- 置顶博客模块 -->
            <div class="p-5 border border-white/10 rounded-xl shadow-lg backdrop-blur-sm">
              <div class="mb-5 flex items-center">
                <div class="i-carbon-star-filled text-xl text-amber-400 mr-2" />
                <h2 class="text-xl text-white font-bold">
                  置顶文章
                </h2>
              </div>
              <div class="gap-5 grid md:grid-cols-2">
                <BlogCard
                  v-for="blog in pinnedBlogs"
                  :key="blog.id"
                  :blog="blog"
                  @click="navigateToBlog"
                />
              </div>
            </div>

            <!-- 最近博客模块 -->
            <div class="p-5 border border-white/10 rounded-xl shadow-lg backdrop-blur-sm">
              <div class="mb-5 flex items-center">
                <div class="i-carbon-recently-viewed text-xl text-teal-400 mr-2" />
                <h2 class="text-xl text-white font-bold">
                  最新内容
                </h2>
              </div>
              <div class="gap-5 grid md:grid-cols-2">
                <BlogCard
                  v-for="blog in recentBlogs"
                  :key="blog.id"
                  :blog="blog"
                  @click="navigateToBlog"
                />
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
