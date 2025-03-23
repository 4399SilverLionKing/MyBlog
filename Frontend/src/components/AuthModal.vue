<script setup lang="ts">
import { ElMessage } from 'element-plus'
import { ref } from 'vue'

defineProps<{
  show: boolean
}>()

const emit = defineEmits<{
  (e: 'close'): void
  (e: 'authSuccess'): void
}>()

const username = ref('')
const password = ref('')
const loading = ref(false)

function authenticate() {
  loading.value = true

  // 模拟认证过程
  setTimeout(() => {
    loading.value = false

    // 简单认证逻辑 (实际项目中应使用更安全的方式)
    if (username.value === 'admin' && password.value === 'admin') {
      emit('authSuccess')
    }
    else {
      ElMessage.error('用户名或密码错误')
    }
  }, 800)
}

function closeModal() {
  username.value = ''
  password.value = ''
  emit('close')
}
</script>

<template>
  <div v-if="show" class="flex items-center inset-0 justify-center fixed z-50">
    <!-- 背景遮罩 -->
    <div class="bg-black/98 inset-0 fixed backdrop-blur-3xl" @click="closeModal" />

    <!-- 模态框内容 -->
    <div class="p-6 border border-gray-800 rounded-lg bg-dark-900/30 max-w-md w-full shadow-xl transform transition-all relative z-10">
      <!-- 标题 -->
      <div class="mb-6 flex items-center justify-between">
        <h2 class="text-xl text-white font-bold">
          管理员登录
        </h2>
        <button
          class="text-gray-400 rounded-full flex h-8 w-8 transition-colors items-center justify-center hover:text-white hover:bg-gray-800"
          @click="closeModal"
        >
          <div class="i-carbon-close text-xl" />
        </button>
      </div>

      <!-- 表单 -->
      <form class="space-y-4" @submit.prevent="authenticate">
        <!-- 用户名 -->
        <div>
          <div class="relative">
            <div class="text-gray-400 transform left-2 top-2.5 absolute">
              <div class="i-carbon-user text-lg" />
            </div>
            <input
              id="username"
              v-model="username"
              autocomplete="off"
              type="text"
              class="text-white py-2 pl-10 pr-4 border border-gray-700 rounded-md bg-dark-800 w-full focus:outline-unset"
              placeholder="请输入用户名"
              required
            >
          </div>
        </div>

        <!-- 密码 -->
        <div>
          <div class="relative">
            <div class="text-gray-400 transform left-2 top-2.5 absolute">
              <div class="i-carbon-password text-lg" />
            </div>
            <input
              id="password"
              v-model="password"
              autocomplete="off"
              type="password"
              class="text-white py-2 pl-10 pr-4 border border-gray-700 rounded-md bg-dark-800 w-full focus:outline-unset"
              placeholder="请输入密码"
              required
            >
          </div>
        </div>

        <!-- 提交按钮 -->
        <div>
          <button
            type="submit"
            class="text-white px-4 py-2 rounded-md bg-teal-600 flex w-full shadow-sm transition-colors items-center justify-center focus:outline-unset hover:bg-teal-700 disabled:opacity-50 disabled:cursor-not-allowed focus:ring-2 focus:ring-teal-500 focus:ring-offset-dark-900 focus:ring-offset-2"
            :disabled="loading"
          >
            <div class="text-gray-400 transform relative">
              <span v-if="loading" class="i-carbon-circle-dash inline-block right-1 top-1 relative animate-spin" />
            </div>
            {{ loading ? '登录中...' : '登录' }}
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
