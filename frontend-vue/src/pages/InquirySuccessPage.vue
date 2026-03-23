<template>
  <div>
    <h2 style="margin:0 0 10px 0;font-size:16px;">提交成功</h2>
    <div style="border:1px solid #eee;border-radius:10px;padding:12px;">
      <div style="font-size:13px;color:#065f46;line-height:1.6;">
        你的线索已提交成功，我们会尽快与您联系。
      </div>

      <div style="margin-top:12px; display:flex; gap:10px; flex-wrap:wrap;">
        <RouterLink
          to="/admin/inquiries"
          style="background:#0f766e;color:#fff;padding:10px 12px;border-radius:8px;text-decoration:none;font-size:14px;"
        >
          查看管理端线索
        </RouterLink>
        <RouterLink
          to="/"
          style="background:#111827;color:#fff;padding:10px 12px;border-radius:8px;text-decoration:none;font-size:14px;"
        >
          返回首页
        </RouterLink>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { setSeoMeta } from '../utils/seo'

onMounted(() => {
  setSeoMeta({
    title: '提交成功 - 农村红木家具',
    description: '你的线索已提交成功，我们将尽快与您联系。',
    url: window.location.href
  })
})
</script>

