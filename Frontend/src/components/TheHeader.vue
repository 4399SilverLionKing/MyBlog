<script setup lang='ts'>
</script>

<template>
  <nav text-xl mt-6 px-40 pb-4 border-b-1 border-gray-300 flex justify-between>
    <div inline-flex gap-10>
      <RouterLink to="/front/home">
        <div hover:text-blue-300>
          Home
        </div>
      </RouterLink>
      <RouterLink to="/front/blogs">
        <div hover:text-blue-300>
          Blogs
        </div>
      </RouterLink>
    </div>
    <div inline-flex gap-4>
      <div>
        <button icon-btn @click="toggleDark()">
          <div i-carbon-sun dark:i-carbon-moon />
        </button>
      </div>
      <div>
        <a
          i-carbon-logo-github icon-btn
          rel="noreferrer"
          href="https://github.com/4399SilverLionKing/MyBlog"
          target="_blank"
          title="GitHub"
        />
      </div>
    </div>
  </nav>
</template>
