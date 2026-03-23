<template>
  <div>
    <h2 style="margin:0 0 12px 0;font-size:16px;">关于我们</h2>

    <section style="border:1px solid #eee;border-radius:10px;padding:12px;margin-bottom:14px;">
      <div style="font-weight:700;margin-bottom:8px;">品牌简介（占位）</div>
      <div style="font-size:13px; line-height:1.8; color:#374151; white-space:pre-wrap;">
        我们专注农村地区的红木家具与门店服务，提供从选材、工艺制作到售后承诺的全流程体验。
        你可以通过“微信咨询”快速了解门店优势与适合你的产品方案。
      </div>
    </section>

    <section style="border:1px solid #eee;border-radius:10px;padding:12px;margin-bottom:14px;">
      <div style="font-weight:700;margin-bottom:8px;">服务流程（占位）</div>
      <div style="font-size:13px; line-height:1.8; color:#374151; white-space:pre-wrap;">
        1）微信/电话联系
        2）确认需求与预算
        3）推荐适合的产品与方案
        4）制作/交付/售后跟进
      </div>
    </section>

    <RouterLink
      to="/contact"
      style="display:inline-block; background:#0f766e; color:#fff; padding:12px 14px; border-radius:10px; text-decoration:none; font-size:14px;"
    >
      联系门店
    </RouterLink>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { setSeoMeta } from '../utils/seo'

onMounted(() => {
  setSeoMeta({
    title: '关于我们 - 农村红木家具',
    description: '了解品牌与服务流程，欢迎联系门店咨询。',
    url: window.location.href
  })
})
</script>

