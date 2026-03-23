<template>
  <div>
    <div v-if="loading" style="padding:12px 0;">加载中...</div>
    <div v-else>
      <h2 style="margin:0 0 10px 0;font-size:16px;">联系我们</h2>
      <div style="border:1px solid #eee;border-radius:10px;padding:12px;">
        <div style="font-weight:700;margin-bottom:6px;">{{ company.companyName }}</div>
        <div style="font-size:13px;color:#333;margin-bottom:8px;">电话：{{ company.phone }}</div>
        <div style="margin-bottom:8px;font-size:13px;color:#333;">地址：{{ company.address }}</div>

        <div style="margin-top:10px; margin-bottom:12px; font-size:13px; color:#666;">
          <a
            :href="mapUrl"
            target="_blank"
            style="display:inline-block; background:#0f766e; color:#fff; padding:10px 12px; border-radius:8px; text-decoration:none; font-size:14px;"
          >
            地图导航
          </a>
        </div>

        <div style="display:flex;gap:10px;flex-wrap:wrap;margin-top:10px;">
          <button
            @click="callPhone(company.phone)"
            style="background:#b45309;color:#fff;padding:10px 12px;border-radius:8px;border:none;font-size:14px;"
          >
            一键拨号
          </button>
          <RouterLink to="/inquiry" style="background:#111827;color:#fff;padding:10px 12px;border-radius:8px;text-decoration:none;font-size:14px;">提交线索（可选）</RouterLink>
        </div>

        <WechatCTA :weChat="company.weChat" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, reactive } from 'vue'
import { getJson } from '../api/client'
import { callPhone } from '../utils/callPhone'
import WechatCTA from '../components/WechatCTA.vue'
import { setSeoMeta } from '../utils/seo'

const state = reactive({
  loading: true,
  company: {}
})

const loading = computed(() => state.loading)
const company = computed(() => state.company)

const mapUrl = computed(() => {
  const addr = state.company?.address || ''
  const lat = state.company?.navLat
  const lng = state.company?.navLng
  const q = lat != null && lng != null ? `${lat},${lng}` : addr
  const query = encodeURIComponent(q || addr || '')
  // 兼容各类内置浏览器/微信：直接打开地图搜索
  return `https://www.google.com/maps/search/?api=1&query=${query}`
})

onMounted(async () => {
  try {
    state.company = await getJson('/api/company')

    setSeoMeta({
      title: state.company?.companyName || '联系我们 - 农村红木家具',
      description: state.company?.seoDescription || state.company?.address || '',
      url: window.location.href
    })
  } catch (e) {
    console.error(e)
    alert('加载失败，请稍后再试。')
  } finally {
    state.loading = false
  }
})
</script>

