<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import AuthModal from '~/components/AuthModal.vue'

// 定义导航项类型
interface NavItem {
  icon: string
  label: string
  goto: string
  isSettings?: boolean // 是否为设置按钮
}

defineProps<{
  navItems: Array<NavItem>
}>()

const router = useRouter()
const showAuthModal = ref(false)

// 处理导航项点击
function handleNavClick(item: NavItem) {
  // 如果是设置按钮，显示认证弹窗
  if (item.isSettings) {
    showAuthModal.value = true
    return false // 阻止默认路由行为
  }
  // 正常路由导航
  return true
}

// 认证成功后处理
function handleAuthSuccess() {
  showAuthModal.value = false
  // 认证成功后跳转到后台页面
  router.push('/back/data')
}
</script>

<template>
  <!-- 左侧导航栏 -->
  <div class="py-8 bg-dark-900/60 flex flex-col h-full w-full items-center justify-between md:py-12 lg:w-24 md:w-20">
    <!-- 主导航按钮 -->
    <nav class="flex flex-col gap-4 items-center">
      <RouterLink
        v-for="(item, index) in navItems.filter(item => !item.isSettings)" :key="index"
        class="group rounded-xl flex h-14 w-14 transition-all items-center justify-center backdrop-blur-sm hover:bg-dark-700/70 hover:scale-[1.05]"
        :to="item.goto"
        :title="item.label"
      >
        <div class="text-2xl text-light-400 transition-all group-hover:text-teal-400" :class="[item.icon]" />
      </RouterLink>
    </nav>

    <!-- 设置按钮（底部） -->
    <div class="mb-4">
      <a
        v-for="(item, index) in navItems.filter(item => item.isSettings)"
        :key="index"
        class="group rounded-xl flex h-14 w-14 transition-all items-center justify-center backdrop-blur-sm hover:bg-dark-700/70 hover:scale-[1.05]"
        href="#"
        :title="item.label"
        @click.prevent="handleNavClick(item)"
      >
        <div class="text-2xl text-light-400 transition-all group-hover:text-teal-400" :class="[item.icon]" />
      </a>
    </div>
  </div>

  <!-- 认证弹窗 -->
  <AuthModal
    :show="showAuthModal"
    @close="showAuthModal = false"
    @auth-success="handleAuthSuccess"
  />
</template>
