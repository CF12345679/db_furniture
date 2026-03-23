<template>
  <div>
    <div v-if="loading" style="padding:12px 0;">加载中...</div>
    <div v-else-if="!product">
      <div>未找到产品</div>
    </div>
    <div v-else>
      <h2 style="margin:0 0 10px 0;font-size:16px;">{{ product.title }}</h2>
      <div style="font-size:13px;color:#666;margin-bottom:10px;">{{ product.summary }}</div>

      <div style="border:1px solid #eee;border-radius:10px;padding:12px; margin-bottom:14px;">
        <div style="font-weight:700;margin-bottom:10px;">图片画廊（占位）</div>
        <div v-if="imageIds.length === 0" style="font-size:13px;color:#666;">暂无图片</div>
        <div v-else>
          <div style="display:flex; gap:12px; flex-wrap:wrap; align-items:flex-start;">
            <div style="flex: 1 1 260px;">
              <img
                :src="currentImageSrc"
                alt="product image"
                style="width:100%; max-width:520px; border:1px solid #eee; border-radius:8px; height:220px; object-fit:cover; background:#fff;"
              />
              <div style="font-size:12px;color:#666;margin-top:8px;">当前：{{ currentImageId }}</div>
            </div>
            <div style="flex: 0 0 auto;">
              <div style="display:flex; flex-direction:column; gap:8px;">
                <button
                  v-for="id in imageIds"
                  :key="id"
                  @click="currentImageId = id"
                  :style="currentImageId === id ? 'border:1px solid #0f766e; background:#ecfdf5;' : 'border:1px solid #e5e7eb; background:#fff;' + 'border-radius:8px; padding:8px; width:120px; text-align:left;'"
                >
                  <div style="font-size:12px; font-weight:700;">{{ id }}</div>
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div style="border:1px solid #eee;border-radius:10px;padding:12px;">
        <div style="font-weight:700;margin-bottom:6px;">工艺/材质特点</div>
        <div style="font-size:13px;color:#333;line-height:1.6;white-space:pre-wrap;">{{ product.materialsCraft }}</div>
        <div style="font-weight:700;margin:14px 0 6px 0;">尺寸/规格</div>
        <div style="font-size:13px;color:#333;line-height:1.6;white-space:pre-wrap;">{{ product.specs }}</div>
      </div>

      <div style="margin-top:14px; display:flex; gap:10px; flex-wrap:wrap;">
        <RouterLink to="/contact" style="background:#0f766e;color:#fff;padding:10px 12px;border-radius:8px;text-decoration:none;font-size:14px;">联系门店</RouterLink>
        <RouterLink to="/contact" style="background:#111827;color:#fff;padding:10px 12px;border-radius:8px;text-decoration:none;font-size:14px;">添加微信咨询</RouterLink>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, reactive, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import { getJson } from '../api/client'
import { setSeoMeta } from '../utils/seo'

const route = useRoute()
const productId = computed(() => route.params.productId)

const state = reactive({
  loading: true,
  product: null
})

const loading = computed(() => state.loading)
const product = computed(() => state.product)

const imageIds = computed(() => {
  const ids = product.value?.imageIds
  return Array.isArray(ids) ? ids : []
})

const currentImageId = ref('')
const currentImageSrc = computed(() => placeholderImageDataUrl(currentImageId.value))

watch(
  () => imageIds.value,
  (ids) => {
    if (!ids || !ids.length) {
      currentImageId.value = ''
      return
    }
    if (!ids.includes(currentImageId.value)) {
      currentImageId.value = ids[0]
    }
  },
  { immediate: true }
)

function placeholderImageDataUrl(id) {
  const safe = (id || '').toString()
  const text = safe.length > 14 ? safe.slice(0, 14) + '...' : safe
  // SVG 占位，不依赖素材文件/网络
  const svg = `
  <svg xmlns="http://www.w3.org/2000/svg" width="800" height="400">
    <defs>
      <linearGradient id="g" x1="0" y1="0" x2="1" y2="1">
        <stop offset="0" stop-color="#f97316" stop-opacity="0.95"/>
        <stop offset="1" stop-color="#0f766e" stop-opacity="0.95"/>
      </linearGradient>
    </defs>
    <rect width="800" height="400" fill="url(#g)"/>
    <rect x="20" y="20" width="760" height="360" rx="22" fill="rgba(255,255,255,0.85)"/>
    <text x="60" y="220" font-family="Arial" font-size="44" font-weight="700" fill="#0f172a">${escapeXml(
      text
    )}</text>
    <text x="60" y="270" font-family="Arial" font-size="20" font-weight="400" fill="#334155">image placeholder</text>
  </svg>
  `.trim()

  return `data:image/svg+xml;charset=utf-8,${encodeURIComponent(svg)}`
}

function escapeXml(s) {
  return (s || '')
    .replaceAll('&', '&amp;')
    .replaceAll('<', '&lt;')
    .replaceAll('>', '&gt;')
    .replaceAll('"', '&quot;')
    .replaceAll("'", '&apos;')
}

onMounted(async () => {
  try {
    const p = await getJson(`/api/products/${encodeURIComponent(productId.value)}`)
    state.product = p

    setSeoMeta({
      title: p?.title || '产品详情 - 农村红木家具',
      description: p?.summary || '',
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

