<template>
  <div>
    <header style="position: sticky; top: 0; background: #fff; z-index: 10; border-bottom: 1px solid #eee;">
      <div style="padding: 12px 16px; display: flex; align-items: center; justify-content: space-between;">
        <div style="font-weight: 700; font-size: 14px;">农村红木家具广告</div>
        <nav style="display: flex; gap: 10px; font-size: 13px;">
          <RouterLink to="/" style="color: #333; text-decoration: none;">首页</RouterLink>
          <RouterLink to="/contact" style="color: #333; text-decoration: none;">联系</RouterLink>
          <RouterLink to="/about" style="color: #333; text-decoration: none;">关于</RouterLink>
          <RouterLink to="/inquiry" style="color: #333; text-decoration: none;">咨询</RouterLink>
        </nav>
      </div>
    </header>

    <main style="padding: 12px 16px;">
      <RouterView />
    </main>
  </div>
</template>

<script setup>
import { RouterLink, RouterView } from 'vue-router'
</script>

