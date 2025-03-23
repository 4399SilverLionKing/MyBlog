<script setup lang="ts">
interface Blog {
  id: number
  title: string
  desc: string
  date: string
  readCount: number
  tags?: string[]
}

defineProps<{
  blog: Blog
  showTags?: boolean // 是否显示标签
}>()

defineEmits<{
  (e: 'click', blogId: number): void
}>()
</script>

<template>
  <div
    class="group p-5 border border-white/10 rounded-xl bg-dark-900/50 cursor-pointer shadow-lg transition-all duration-300 hover:shadow-dark-950/50 hover:translate-y-[-4px]"
    @click="$emit('click', blog.id)"
  >
    <div class="relative">
      <!-- 博客标题 -->
      <h3
        class="text-white font-semibold transition-all duration-300 group-hover:text-teal-300"
        :class="showTags ? 'text-xl font-bold mb-3' : 'text-lg mb-2'"
      >
        {{ blog.title }}
      </h3>

      <!-- 博客描述 -->
      <p
        v-if="blog.desc"
        class="text-sm text-light-300/90 mb-3 transition-all duration-300 line-clamp-2 group-hover:text-light-200"
      >
        {{ blog.desc }}
      </p>

      <!-- 元数据区域 -->
      <div
        class="text-xs text-light-400/90 flex gap-3 transition-all duration-300 items-center justify-between group-hover:text-light-300"
      >
        <div class="flex gap-3">
          <span class="flex items-center" :class="showTags ? 'date' : ''">
            <div class="i-carbon-calendar mr-1.5 opacity-70" />
            {{ blog.date }}
          </span>
          <span class="flex items-center">
            <div class="i-carbon-view mr-1.5 opacity-70" />
            {{ blog.readCount }}
          </span>
        </div>
        <!-- 标签区域 -->
        <div v-if="showTags && blog.tags" class="flex flex-wrap gap-2">
          <span
            v-for="tag in blog.tags"
            :key="tag"
            class="text-xs text-light-300 px-2.5 py-1 border border-white/5 rounded-full bg-dark-950/30"
          >
            {{ tag }}
          </span>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.group {
  position: relative;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.group::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(45deg, transparent, rgba(172, 172, 172, 0.3), transparent);
  opacity: 0;
  transition: opacity 0.5s ease;
}

.group:hover::before {
  opacity: 1;
}
</style>
