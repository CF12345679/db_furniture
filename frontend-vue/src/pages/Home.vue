<template>
  <div>
    <section style="background:#f7f7f7;border-radius:10px;padding:12px;">
      <div v-if="loading">加载中...</div>
      <div v-else>
        <div style="font-size:13px;color:#666;margin-bottom:8px;">门店信息与获客入口</div>
        <div style="font-weight:700;margin-bottom:6px;">{{ company.companyName }}</div>
        <div style="font-size:13px;color:#333;margin-bottom:10px;">电话：{{ company.phone }}</div>
        <div style="display:flex;gap:10px;flex-wrap:wrap;">
          <a :href="`tel:${company.phone}`" style="background:#b45309;color:#fff;padding:10px 12px;border-radius:8px;text-decoration:none;font-size:14px;">拨打电话</a>
          <button @click="goInquiry" style="background:#111827;color:#fff;padding:10px 12px;border-radius:8px;border:none;font-size:14px;">添加微信咨询</button>
        </div>
      </div>
    </section>

    <section style="margin-top:14px;">
      <h3 style="margin:0 0 10px 0;font-size:15px;">推荐产品</h3>
      <div v-if="loading">加载中...</div>
      <div v-else style="display:flex;gap:10px;flex-wrap:wrap;">
        <div
          v-for="p in featuredProducts"
          :key="p.id"
          style="border:1px solid #eee;border-radius:10px;padding:10px;width:calc(50% - 5px);box-sizing:border-box;"
        >
          <div style="font-weight:700;font-size:13px;margin-bottom:6px;">{{ p.title }}</div>
          <div style="font-size:12px;color:#666;min-height:34px;line-height:1.4;overflow:hidden;">{{ p.summary }}</div>
          <RouterLink :to="`/products/${p.id}`" style="display:inline-block;margin-top:8px;color:#0f766e;text-decoration:none;font-size:13px;">查看详情</RouterLink>
        </div>
      </div>
    </section>

    <section style="margin-top:14px;">
      <h3 style="margin:0 0 10px 0;font-size:15px;">分类入口</h3>
      <div v-if="loading">加载中...</div>
      <div v-else style="display:flex;gap:10px;flex-wrap:wrap;">
        <button
          v-for="c in categories"
          :key="c.id"
          @click="goCategory(c.id)"
          style="border:1px solid #e5e7eb;background:#fff;border-radius:999px;padding:8px 12px;font-size:13px;"
        >
          {{ c.name }}
        </button>
      </div>
    </section>
  </div>
</template>

<script setup>
import { computed, onMounted, reactive } from 'vue'
import { getJson } from '../api/client'
import { useRouter } from 'vue-router'

const router = useRouter()

const state = reactive({
  loading: true,
  company: { companyName: '', phone: '' },
  banners: [],
  categories: [],
  products: [],
  cases: []
})

const loading = computed(() => state.loading)
const company = computed(() => state.company)
const categories = computed(() => state.categories)
const featuredProducts = computed(() => state.products.slice(0, 4))

function goInquiry() {
  router.push('/inquiry')
}
function goCategory(id) {
  router.push(`/categories/${id}`)
}

onMounted(async () => {
  try {
    const [banners, categoriesRes, productsRes, casesRes, companyRes] = await Promise.all([
      getJson('/api/banners'),
      getJson('/api/categories'),
      getJson('/api/products'),
      getJson('/api/cases'),
      getJson('/api/company')
    ])
    state.banners = banners
    state.categories = categoriesRes
    state.products = productsRes
    state.cases = casesRes || []
    state.company = companyRes
  } catch (e) {
    console.error(e)
    alert('加载失败，请稍后再试。')
  } finally {
    state.loading = false
  }
})
</script>

