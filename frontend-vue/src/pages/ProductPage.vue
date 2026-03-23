<template>
  <div>
    <div v-if="loading" style="padding:12px 0;">加载中...</div>
    <div v-else-if="!product">
      <div>未找到产品</div>
    </div>
    <div v-else>
      <h2 style="margin:0 0 10px 0;font-size:16px;">{{ product.title }}</h2>
      <div style="font-size:13px;color:#666;margin-bottom:10px;">{{ product.summary }}</div>

      <div style="border:1px solid #eee;border-radius:10px;padding:12px;">
        <div style="font-weight:700;margin-bottom:6px;">工艺/材质特点</div>
        <div style="font-size:13px;color:#333;line-height:1.6;white-space:pre-wrap;">{{ product.materialsCraft }}</div>
        <div style="font-weight:700;margin:14px 0 6px 0;">尺寸/规格</div>
        <div style="font-size:13px;color:#333;line-height:1.6;white-space:pre-wrap;">{{ product.specs }}</div>
      </div>

      <div style="margin-top:14px; display:flex; gap:10px; flex-wrap:wrap;">
        <RouterLink to="/contact" style="background:#0f766e;color:#fff;padding:10px 12px;border-radius:8px;text-decoration:none;font-size:14px;">联系门店</RouterLink>
        <RouterLink to="/inquiry" style="background:#111827;color:#fff;padding:10px 12px;border-radius:8px;text-decoration:none;font-size:14px;">添加微信咨询</RouterLink>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, reactive } from 'vue'
import { useRoute } from 'vue-router'
import { getJson } from '../api/client'

const route = useRoute()
const productId = computed(() => route.params.productId)

const state = reactive({
  loading: true,
  product: null
})

const loading = computed(() => state.loading)
const product = computed(() => state.product)

onMounted(async () => {
  try {
    const p = await getJson(`/api/products/${encodeURIComponent(productId.value)}`)
    state.product = p
  } catch (e) {
    console.error(e)
    alert('加载失败，请稍后再试。')
  } finally {
    state.loading = false
  }
})
</script>

