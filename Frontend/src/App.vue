<script setup lang='ts'>
import { ref, watch } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const routePath = ref(route.fullPath)

watch(() => route.fullPath, (newPath) => {
  console.warn('路由路径变化为:', newPath)
  routePath.value = newPath
})
</script>

<template>
  <template v-if="routePath.includes('front')">
    <TheHeader />
    <main font-sans p="x-4 y-10" text="center gray-700 dark:gray-200">
      <RouterView />
      <TheFooter />
    </main>
  </template>
  <template v-if="routePath.includes('back')">
    <main font-sans p="x-4 y-10" text="center gray-700 dark:gray-200">
      <RouterView />
    </main>
  </template>
</template>
