<script setup lang='ts'>
import { computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import BlogCard from '~/components/BlogCard.vue'
import { useBlogStore } from '~/stores/blogStore'

// 初始化路由器
const router = useRouter()

// 使用博客store
const blogStore = useBlogStore()

// 背景图片
const background = '/images/XIn.png'

// 博客分类
const categories = [
  { id: 'all', name: 'All', icon: 'i-carbon-list' },
  { id: 'code', name: 'Code', icon: 'i-carbon-code' },
  { id: 'life', name: 'Life', icon: 'i-carbon-sun' },
  { id: 'read', name: 'Read', icon: 'i-carbon-book' },
]

// 使用store中的activeCategory
const activeCategory = computed(() => blogStore.activeCategory)

// 使用store中的searchQuery
const searchQuery = computed({
  get: () => blogStore.searchQuery,
  set: (value: string) => blogStore.setSearchQuery(value),
})

// 直接使用store中的filteredBlogs
const filteredBlogs = computed(() => blogStore.filteredBlogs)

function setCategory(category: string) {
  blogStore.setCategory(category)
}

function goBack() {
  router.push('/front/home')
}

function navigateToDetail(blogId: number) {
  // 使用store处理博客导航
  const blog = blogStore.getBlogById(blogId)
  if (blog) {
    blogStore.setCurrentBlog(blog)
    router.push('/front/detail')
  }
}

// 页面加载时获取博客列表
onMounted(() => {
  // 确保先设置默认分类
  blogStore.setCategory('all')
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
    <div class="p-4 flex h-full w-full items-start justify-center relative">
      <div class="border border-gray-900 rounded-3xl flex h-full w-full shadow-xl overflow-hidden backdrop-blur-sm">
        <!-- 左侧分类边栏，高度为屏幕高度，宽度缩小 -->
        <div class="py-8 bg-dark-900/60 flex flex-col w-full items-center justify-between md:py-12 lg:w-24 md:w-20">
          <!-- 分类列表 -->
          <ul class="flex flex-col w-full items-center">
            <li
              v-for="category in categories"
              :key="category.id"
              class="mb-4 rounded-xl flex h-12 w-12 cursor-pointer transition-all items-center justify-center"
              :title="category.name"
              :class="activeCategory === category.id
                ? 'bg-teal-500/40 text-white'
                : 'text-light-400 hover:bg-dark-700/70 hover:text-teal-400'"
              @click="setCategory(category.id)"
            >
              <div :class="[category.icon]" class="text-2xl" />
            </li>
          </ul>

          <!-- 返回按钮 -->
          <div class="mb-6">
            <button
              class="text-light-400 rounded-xl flex h-12 w-12 transition-all items-center justify-center hover:text-teal-400 hover:bg-dark-700/70"
              @click="goBack"
            >
              <div class="i-carbon-arrow-left text-2xl" />
            </button>
          </div>
        </div>

        <!-- 右侧主内容区域 -->
        <div class="p-6 flex-1 overflow-auto">
          <!-- 顶部导航栏 -->
          <div class="mb-8 flex items-center justify-center">
            <div class="max-w-2xl w-2/3 relative">
              <input
                v-model="searchQuery"
                type="text"
                placeholder="Search"
                class="text-white py-3 pl-12 pr-4 outline-unset border border-white/10 rounded-full bg-dark-800/50 w-full shadow-lg transition-all backdrop-blur-sm placeholder:text-gray-400 focus:border-teal-500/50 hover:border-teal-500/30 focus:bg-dark-800/80"
              >
              <div class="i-carbon-search text-xl left-4 top-3.5 absolute" />
            </div>
          </div>

          <!-- 博客卡片 -->
          <div class="mx-20 gap-x-20 gap-y-10 grid grid-cols-2">
            <BlogCard
              v-for="blog in filteredBlogs"
              :key="blog.id"
              :blog="blog"
              show-tags
              @click="navigateToDetail"
            />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
