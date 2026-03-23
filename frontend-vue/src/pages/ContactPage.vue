<template>
  <div>
    <div v-if="loading" style="padding:12px 0;">加载中...</div>
    <div v-else>
      <h2 style="margin:0 0 10px 0;font-size:16px;">联系我们</h2>
      <div style="border:1px solid #eee;border-radius:10px;padding:12px;">
        <div style="font-weight:700;margin-bottom:6px;">{{ company.companyName }}</div>
        <div style="font-size:13px;color:#333;margin-bottom:8px;">电话：{{ company.phone }}</div>
        <div style="margin-bottom:8px;font-size:13px;color:#333;">地址：{{ company.address }}</div>

        <div style="display:flex;gap:10px;flex-wrap:wrap;margin-top:10px;">
          <a :href="`tel:${company.phone}`" style="background:#b45309;color:#fff;padding:10px 12px;border-radius:8px;text-decoration:none;font-size:14px;">一键拨号</a>
          <RouterLink to="/inquiry" style="background:#111827;color:#fff;padding:10px 12px;border-radius:8px;text-decoration:none;font-size:14px;">添加微信咨询</RouterLink>
        </div>

        <div style="margin-top:12px; font-size:12px; color:#666;">
          微信入口（占位）：{{ company.weChat }}
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, reactive } from 'vue'
import { getJson } from '../api/client'

const state = reactive({
  loading: true,
  company: {}
})

const loading = computed(() => state.loading)
const company = computed(() => state.company)

onMounted(async () => {
  try {
    state.company = await getJson('/api/company')
  } catch (e) {
    console.error(e)
    alert('加载失败，请稍后再试。')
  } finally {
    state.loading = false
  }
})
</script>

