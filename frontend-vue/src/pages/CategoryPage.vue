<template>
  <div>
    <div v-if="loading" style="padding:12px 0;">加载中...</div>
    <div v-else>
      <h2 style="margin:0 0 12px 0;font-size:16px;">{{ categoryName }}</h2>

      <div style="display:flex;gap:10px;flex-wrap:wrap;margin-bottom:12px;">
        <button
          v-for="c in categories"
          :key="c.id"
          @click="goCategory(c.id)"
          style="border:1px solid #e5e7eb;background:#fff;border-radius:999px;padding:8px 12px;font-size:13px;"
        >
          {{ c.name }}
        </button>
      </div>

      <div style="display:flex;gap:10px;flex-wrap:wrap;">
        <div
          v-for="p in products"
          :key="p.id"
          style="border:1px solid #eee;border-radius:10px;padding:10px;width:calc(50% - 5px);box-sizing:border-box;"
        >
          <div style="font-weight:700;font-size:13px;margin-bottom:6px;">{{ p.title }}</div>
          <div style="font-size:12px;color:#666;min-height:34px;line-height:1.4;overflow:hidden;">{{ p.summary }}</div>
          <RouterLink :to="`/products/${p.id}`" style="display:inline-block;margin-top:8px;color:#0f766e;text-decoration:none;font-size:13px;">查看详情</RouterLink>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, reactive } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getJson } from '../api/client'
import { setSeoMeta } from '../utils/seo'

const route = useRoute()
const router = useRouter()
const categoryId = computed(() => route.params.categoryId)

const state = reactive({
  loading: true,
  categories: [],
  products: []
})

const loading = computed(() => state.loading)
const categories = computed(() => state.categories)
const products = computed(() => state.products)

const categoryName = computed(() => {
  const c = state.categories.find(x => x.id === categoryId.value)
  return c ? c.name : '分类'
})

function goCategory(id) {
  router.push(`/categories/${id}`)
}

onMounted(async () => {
  try {
    const [categoriesRes, productsRes] = await Promise.all([
      getJson('/api/categories'),
      getJson(`/api/products?categoryId=${encodeURIComponent(categoryId.value)}`)
    ])
    state.categories = categoriesRes
    state.products = productsRes

    const c = Array.isArray(categoriesRes)
      ? categoriesRes.find(x => x.id === categoryId.value)
      : null
    setSeoMeta({
      title: (c && c.name ? c.name : '分类') + ' - 农村红木家具',
      description: '',
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

