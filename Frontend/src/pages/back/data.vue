<script setup lang="ts">
import type { BlogList } from '~/stores/blogStore'
import { computed } from 'vue'
import SideBar from '~/components/SideBar.vue'
import { useBlogStore } from '~/stores/blogStore'

// 获取博客store
const blogStore = useBlogStore()

// 导航栏数据
const navItems = [
  { icon: 'i-carbon-home', label: 'Home', goto: '/front/home' },
  { icon: 'i-carbon-dashboard', label: 'Data', goto: '/back/data' },
  { icon: 'i-carbon-document', label: 'Manage', goto: '/back/manage' },
]

// 博客系统背景图片
const background = '/images/BlackSea.png'

// 计算统计数据
const totalBlogs = computed(() => blogStore.blogs.length)
const totalReadCount = computed(() => blogStore.blogs.reduce((sum: number, blog: BlogList) => sum + blog.readCount, 0))
const totalTags = computed(() => new Set(blogStore.blogs.flatMap((blog: BlogList) => blog.tags || [])).size)

// 获取按分类分组的博客
const blogsByCategory = computed(() => {
  const categories: Record<string, number> = {}
  blogStore.blogs.forEach((blog: BlogList) => {
    if (!categories[blog.category]) {
      categories[blog.category] = 0
    }
    categories[blog.category]++
  })
  return categories
})

// 获取阅读量最高的博客
const topReadBlogs = computed(() => {
  return [...blogStore.blogs]
    .sort((a: BlogList, b: BlogList) => b.readCount - a.readCount)
    .slice(0, 5)
})

// 获取标签统计
const tagStats = computed(() => {
  const tags: Record<string, number> = {}
  blogStore.blogs.forEach((blog: BlogList) => {
    blog.tags?.forEach((tag: string) => {
      if (!tags[tag]) {
        tags[tag] = 0
      }
      tags[tag]++
    })
  })
  return Object.entries(tags)
    .sort((a, b) => b[1] - a[1])
    .slice(0, 10)
    .map(([tag, count]) => ({ tag, count }))
})

// 分类数据对应的样式
const categoryStyles: Record<string, { icon: string, bgClass: string, textClass: string }> = {
  code: {
    icon: 'i-carbon-code',
    bgClass: 'bg-teal-500/60',
    textClass: 'text-teal-400',
  },
  read: {
    icon: 'i-carbon-book',
    bgClass: 'bg-purple-500/60',
    textClass: 'text-purple-400',
  },
  life: {
    icon: 'i-carbon-camera',
    bgClass: 'bg-blue-500/60',
    textClass: 'text-blue-400',
  },
}

// 获取分类标签
function getCategoryLabel(category: string): string {
  const labels: Record<string, string> = {
    code: '代码',
    read: '阅读',
    life: '生活',
  }
  return labels[category] || category
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
          <!-- 统计卡片区域 -->
          <div class="mb-8 gap-6 grid grid-cols-1 md:grid-cols-3">
            <div class="p-6 border border-white/10 rounded-xl bg-dark-900/50 flex items-center backdrop-blur-sm">
              <div class="mr-4 rounded-xl bg-teal-500/20 flex h-12 w-12 items-center justify-center">
                <div class="i-carbon-document text-2xl text-teal-400" />
              </div>
              <div>
                <p class="text-sm text-light-400">
                  总博客数
                </p>
                <h3 class="text-2xl text-white font-bold">
                  {{ totalBlogs }}
                </h3>
              </div>
            </div>

            <div class="p-6 border border-white/10 rounded-xl bg-dark-900/50 flex items-center backdrop-blur-sm">
              <div class="mr-4 rounded-xl bg-purple-500/20 flex h-12 w-12 items-center justify-center">
                <div class="i-carbon-view text-2xl text-purple-400" />
              </div>
              <div>
                <p class="text-sm text-light-400">
                  总阅读量
                </p>
                <h3 class="text-2xl text-white font-bold">
                  {{ totalReadCount }}
                </h3>
              </div>
            </div>

            <div class="p-6 border border-white/10 rounded-xl bg-dark-900/50 flex items-center backdrop-blur-sm">
              <div class="mr-4 rounded-xl bg-blue-500/20 flex h-12 w-12 items-center justify-center">
                <div class="i-carbon-tag text-2xl text-blue-400" />
              </div>
              <div>
                <p class="text-sm text-light-400">
                  标签数量
                </p>
                <h3 class="text-2xl text-white font-bold">
                  {{ totalTags }}
                </h3>
              </div>
            </div>
          </div>

          <!-- 数据内容区 -->
          <div class="gap-6 grid grid-cols-1 md:grid-cols-2">
            <!-- 左侧数据 -->
            <div class="space-y-6">
              <!-- 分类统计 -->
              <div class="p-5 border border-white/10 rounded-xl shadow-lg backdrop-blur-sm">
                <div class="mb-5 flex items-center">
                  <div class="i-carbon-category text-xl text-teal-400 mr-2" />
                  <h2 class="text-xl text-white font-bold">
                    博客分类统计
                  </h2>
                </div>

                <div class="space-y-3">
                  <div
                    v-for="(count, category) in blogsByCategory"
                    :key="category"
                    class="p-3 border border-white/10 rounded-lg bg-dark-900/30 flex items-center justify-between"
                  >
                    <div class="flex items-center">
                      <div
                        class="mr-3 rounded-lg flex h-10 w-10 items-center justify-center"
                        :class="categoryStyles[category]?.bgClass || 'bg-gray-500/20'"
                      >
                        <div
                          class="text-xl"
                          :class="categoryStyles[category]?.icon || 'i-carbon-document'"
                        />
                      </div>
                      <div>
                        <h4
                          class="font-medium"
                          :class="categoryStyles[category]?.textClass || 'text-light-300'"
                        >
                          {{ getCategoryLabel(category) }}
                        </h4>
                        <p class="text-xs text-light-400">
                          {{ count }} 篇博客
                        </p>
                      </div>
                    </div>

                    <div class="text-xl text-light-300 font-medium">
                      {{ Math.round(count / totalBlogs * 100) }}%
                    </div>
                  </div>
                </div>
              </div>

              <!-- 热门标签 -->
              <div class="p-5 border border-white/10 rounded-xl shadow-lg backdrop-blur-sm">
                <div class="mb-5 flex items-center">
                  <div class="i-carbon-tag-group text-xl text-purple-400 mr-2" />
                  <h2 class="text-xl text-white font-bold">
                    热门标签
                  </h2>
                </div>

                <div class="flex flex-wrap gap-2">
                  <div
                    v-for="item in tagStats"
                    :key="item.tag"
                    class="px-3 py-1.5 border border-white/10 rounded-lg bg-dark-900/30 flex items-center"
                  >
                    <span class="text-teal-400 mr-2">{{ item.tag }}</span>
                    <span class="text-xs text-light-400 px-1.5 py-0.5 rounded-full bg-dark-800/50">
                      {{ item.count }}
                    </span>
                  </div>
                </div>
              </div>
            </div>

            <!-- 右侧数据 -->
            <div class="space-y-6">
              <!-- 阅读量最高的博客 -->
              <div class="p-5 border border-white/10 rounded-xl shadow-lg backdrop-blur-sm">
                <div class="mb-5 flex items-center">
                  <div class="i-carbon-view text-xl text-blue-400 mr-2" />
                  <h2 class="text-xl text-white font-bold">
                    阅读量最高的博客
                  </h2>
                </div>

                <div class="space-y-3">
                  <div
                    v-for="blog in topReadBlogs"
                    :key="blog.id"
                    class="p-3 border border-white/10 rounded-lg bg-dark-900/30 flex items-center justify-between"
                  >
                    <div>
                      <h4 class="text-light-100 font-medium">
                        {{ blog.title }}
                      </h4>
                      <div class="text-xs mt-1 flex items-center">
                        <span
                          class="mr-2 px-1.5 py-0.5 rounded"
                          :class="{
                            'bg-teal-500/20 text-teal-400': blog.category === 'code',
                            'bg-purple-500/20 text-purple-400': blog.category === 'read',
                            'bg-blue-500/20 text-blue-400': blog.category === 'life',
                          }"
                        >
                          {{ getCategoryLabel(blog.category) }}
                        </span>
                        <span class="text-light-400">{{ blog.date }}</span>
                      </div>
                    </div>

                    <div class="text-light-300 flex items-center">
                      <div class="i-carbon-view mr-1 opacity-70" />
                      {{ blog.readCount }}
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
