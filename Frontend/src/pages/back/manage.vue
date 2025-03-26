<script setup lang="ts">
import { ElMessage, ElMessageBox } from 'element-plus'
import { computed, onMounted, ref, watch } from 'vue'
import { useRouter } from 'vue-router'
import SideBar from '~/components/SideBar.vue'
import { useBlogStore } from '~/stores/blogStore'

// 定义导航项
const navItems = [
  { icon: 'i-carbon-home', label: 'Home', goto: '/front/home' },
  { icon: 'i-carbon-dashboard', label: 'Data', goto: '/back/data' },
  { icon: 'i-carbon-document', label: 'Manage', goto: '/back/manage' },
]

// 使用博客store
const blogStore = useBlogStore()
const router = useRouter()
// 搜索关键词
const searchKeyword = ref('')
// 当前选择的分类
const selectedCategory = ref('all')

// 监听搜索关键词变化，触发搜索
watch(searchKeyword, (newVal) => {
  blogStore.setSearchQuery(newVal)
  blogStore.fetchBlogs()
})

// 获取loading和过滤后的博客列表
const loading = computed(() => blogStore.loading)
const filteredBlogs = computed(() => {
  if (selectedCategory.value === 'all') {
    return blogStore.filteredBlogs
  }
  else {
    return blogStore.filteredBlogs.filter(blog => blog.category === selectedCategory.value)
  }
})

// 博客系统背景图片
const background = '/images/BlackSea.png'

// 页面加载时获取博客列表
onMounted(() => {
  blogStore.fetchBlogs()
})

// 切换分类
function changeCategory(category: string) {
  selectedCategory.value = category
  // 设置store中的分类过滤并重新获取数据
  blogStore.setCategory(category)
  blogStore.fetchBlogs()
}

// 打开新增博客弹窗
function openAddModal() {
  router.push('/back/update')
}

// 打开编辑博客弹窗
function openEditModal(blog: any) {
  router.push(`/back/update?id=${blog.id}`)
}

// 删除博客
async function deleteBlog(id: number) {
  try {
    await ElMessageBox.confirm('确定要删除这篇博客吗？', '警告', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
      customClass: 'dark-message-box',
      confirmButtonClass: 'el-button--danger',
      cancelButtonClass: 'el-button--info',
    })

    await blogStore.removeBlog(id)
    ElMessage({
      message: '博客删除成功',
      type: 'success',
      customClass: 'dark-message',
    })
  }
  catch (error) {
    if (error !== 'cancel') {
      ElMessage({
        message: `删除失败: ${(error as Error).message}`,
        type: 'error',
        customClass: 'dark-message',
      })
    }
  }
}

const categories = [
  { label: '代码', value: 'code' },
  { label: '阅读', value: 'read' },
  { label: '生活', value: 'life' },
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
          <!-- 顶部标题区域 -->
          <div class="mb-6 flex items-center justify-between">
            <!-- 分类筛选按钮 -->
            <div class="flex gap-2">
              <button
                class="text-sm px-3 py-1.5 rounded-lg transition-colors"
                :class="selectedCategory === 'all' ? 'bg-gray-600/80 text-white' : 'bg-dark-800/70 text-light-300 hover:bg-dark-700/70'"
                @click="changeCategory('all')"
              >
                All
              </button>
              <button
                class="text-sm px-3 py-1.5 rounded-lg transition-colors"
                :class="selectedCategory === 'code' ? 'bg-teal-600/80 text-white' : 'bg-dark-800/70 text-light-300 hover:bg-dark-700/70'"
                @click="changeCategory('code')"
              >
                Code
              </button>
              <button
                class="text-sm px-3 py-1.5 rounded-lg transition-colors"
                :class="selectedCategory === 'life' ? 'bg-blue-600/80 text-white' : 'bg-dark-800/70 text-light-300 hover:bg-dark-700/70'"
                @click="changeCategory('life')"
              >
                Life
              </button>
              <button
                class="text-sm px-3 py-1.5 rounded-lg transition-colors"
                :class="selectedCategory === 'read' ? 'bg-purple-600/80 text-white' : 'bg-dark-800/70 text-light-300 hover:bg-dark-700/70'"
                @click="changeCategory('read')"
              >
                Read
              </button>
            </div>
            <!-- 搜索框 -->
            <div class="w-150 relative">
              <input
                v-model="searchKeyword"
                type="text"
                placeholder="Search"
                class="text-sm text-light-200 px-4 py-2 pl-10 border border-white/10 rounded-lg bg-dark-800/70 w-full focus:outline-unset focus:ring-1 focus:ring-teal-500"
              >
              <div class="i-carbon-search text-light-400 left-3 top-2.5 absolute" />
            </div>

            <!-- 添加博客按钮 -->
            <button
              class="text-white px-4 py-2 rounded-lg bg-gray-600/80 flex gap-2 transition-colors items-center backdrop-blur-sm hover:bg-teal-500"
              @click="openAddModal"
            >
              <span>Add</span>
            </button>
          </div>

          <!-- 博客列表容器 -->
          <div class="p-5 border border-white/10 rounded-xl shadow-lg backdrop-blur-sm">
            <!-- 博客列表 -->
            <div class="rounded-xl overflow-hidden">
              <div v-if="loading" class="py-12 text-center bg-dark-900/30">
                <div class="i-carbon-circle-dash text-6xl text-teal-400/50 mx-auto mb-4 animate-spin" />
                <p class="text-light-400">
                  加载中...
                </p>
              </div>

              <div v-else-if="filteredBlogs.length === 0" class="py-12 text-center bg-dark-900/30">
                <div class="i-carbon-document-blank text-6xl text-light-400/50 mx-auto mb-4" />
                <p class="text-light-400">
                  暂无博客文章
                </p>
              </div>

              <!-- 列表内容 -->
              <div v-for="blog in filteredBlogs" v-else :key="blog.id" class="p-4 border-b border-white/5 bg-dark-900/30 gap-4 grid grid-cols-5 transition-colors items-center hover:bg-dark-700/30">
                <!-- 标题 -->
                <div class="col-span-2">
                  <h3 class="text-white font-medium truncate">
                    {{ blog.title }}
                  </h3>
                  <p class="text-sm text-light-400 mt-1 line-clamp-1">
                    {{ blog.subtitle }}
                  </p>
                  <div class="mt-2 flex flex-wrap gap-1 justify-center">
                    <span
                      v-for="tag in blog.tags"
                      :key="tag"
                      class="text-xs text-teal-400 px-2 py-0.5 border border-teal-500/20 rounded-full bg-dark-800/50"
                    >
                      {{ tag }}
                    </span>
                  </div>
                </div>
                <!-- 分类 -->
                <div class="flex items-center justify-center">
                  <span
                    class="text-xs px-2 py-1 rounded-md"
                    :class="{
                      'bg-teal-500/20 text-teal-400': blog.category === 'code',
                      'bg-purple-500/20 text-purple-400': blog.category === 'read',
                      'bg-blue-500/20 text-blue-400': blog.category === 'life',
                    }"
                  >
                    {{ categories.find(c => c.value === blog.category)?.label || blog.category }}
                  </span>
                </div>
                <!-- 日期 -->
                <div class="text-light-300 text-center">
                  {{ blog.date }}
                </div>
                <!-- 操作 -->
                <div class="flex gap-3 justify-center">
                  <button
                    class="text-blue-400 p-1.5 rounded-lg transition-colors hover:text-blue-300 hover:bg-blue-500/10"
                    title="编辑"
                    @click="openEditModal(blog)"
                  >
                    <div class="i-carbon-edit text-lg" />
                  </button>

                  <button
                    class="text-red-400 p-1.5 rounded-lg transition-colors hover:text-red-300 hover:bg-red-500/10"
                    title="删除"
                    @click="deleteBlog(blog.id)"
                  >
                    <div class="i-carbon-trash-can text-lg" />
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
:deep(input),
:deep(textarea),
:deep(select) {
  background-color: rgba(30, 30, 30, 0.5);
}

:deep(input:focus),
:deep(textarea:focus),
:deep(select:focus) {
  box-shadow: 0 0 0 2px rgba(20, 184, 166, 0.2);
}

/* 暗色主题消息框样式 */
:global(.dark-message-box) {
  background-color: #1e1e1e !important;
  border: 1px solid rgba(255, 255, 255, 0.1) !important;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.5) !important;
}

:global(.dark-message-box .el-message-box__title) {
  color: rgba(123, 121, 121, 0.9) !important;
}

:global(.dark-message-box .el-message-box__content) {
  color: rgba(255, 255, 255, 0.7) !important;
}

:global(.dark-message-box .el-message-box__headerbtn .el-message-box__close) {
  color: rgba(255, 255, 255, 0.6) !important;
}

:global(.dark-message-box .el-button--default) {
  background-color: #333 !important;
  border-color: #444 !important;
  color: rgba(255, 255, 255, 0.7) !important;
}

:global(.dark-message) {
  background-color: #1e1e1e !important;
  border: 1px solid rgba(255, 255, 255, 0.1) !important;
  color: rgba(255, 255, 255, 0.8) !important;
}

:global(.dark-message .el-message__content) {
  color: rgba(255, 255, 255, 0.8) !important;
}

:global(.dark-message .el-message__icon) {
  color: inherit !important;
}
</style>
