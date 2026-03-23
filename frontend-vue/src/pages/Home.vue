<template>
  <div>
    <!-- Banners -->
    <section
      v-if="activeBanners.length"
      style="margin-bottom:14px;border-radius:12px; padding:14px; background:linear-gradient(135deg,#f97316,#0f766e); color:#fff;"
    >
      <div style="font-size:13px; opacity:0.95; margin-bottom:8px;">门店推荐</div>
      <div style="font-weight:800; font-size:16px; margin-bottom:10px; word-break:break-word;">
        {{ currentBanner?.title }}
      </div>

      <div style="display:flex; gap:10px; flex-wrap:wrap; align-items:center; justify-content:space-between;">
        <button
          @click="onBannerCtaClick(currentBanner)"
          style="background:#fff;color:#0f766e;border:none;border-radius:10px;padding:10px 12px;font-size:14px; font-weight:700;"
        >
          {{ bannerCtaText }}
        </button>

        <div style="display:flex; gap:8px; align-items:center;">
          <div
            v-for="(b, idx) in activeBanners"
            :key="b.id"
            @click="setBannerIndex(idx)"
            style="width:8px;height:8px;border-radius:999px; cursor:pointer; background: idx === bannerIndex ? '#fff' : 'rgba(255,255,255,0.45)';"
          ></div>
        </div>
      </div>
    </section>

    <section style="background:#f7f7f7;border-radius:10px;padding:12px;">
      <div v-if="loading">加载中...</div>
      <div v-else>
        <div style="font-size:13px;color:#666;margin-bottom:8px;">门店信息与获客入口</div>
        <div style="font-weight:700;margin-bottom:6px;">{{ company.companyName }}</div>
        <div style="font-size:13px;color:#333;margin-bottom:10px;">电话：{{ company.phone }}</div>
        <div style="display:flex;gap:10px;flex-wrap:wrap;">
          <button
            @click="callPhone(company.phone)"
            style="background:#b45309;color:#fff;padding:10px 12px;border-radius:8px;border:none;font-size:14px;"
          >
            拨打电话
          </button>
          <button @click="goInquiry" style="background:#111827;color:#fff;padding:10px 12px;border-radius:8px;border:none;font-size:14px;">提交线索（可选）</button>
        </div>

        <WechatCTA :weChat="company.weChat" />
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

    <!-- Cases -->
    <section style="margin-top:14px;">
      <h3 style="margin:0 0 10px 0;font-size:15px;">客户案例</h3>
      <div v-if="loading">加载中...</div>
      <div v-else>
        <div v-if="cases.length === 0" style="font-size:13px;color:#666;">暂无案例</div>
        <div v-else style="display:flex; gap:10px; flex-wrap:wrap;">
          <div
            v-for="c in cases"
            :key="c.id"
            style="border:1px solid #eee; border-radius:10px; padding:12px; width:calc(50% - 5px); box-sizing:border-box;"
          >
            <div style="font-weight:700; font-size:13px; margin-bottom:6px;">{{ c.title }}</div>
            <div style="font-size:12px;color:#666; line-height:1.6; white-space:pre-wrap;">{{ c.content }}</div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { computed, onMounted, onUnmounted, reactive, ref } from 'vue'
import { getJson } from '../api/client'
import { useRouter } from 'vue-router'
import WechatCTA from '../components/WechatCTA.vue'
import { callPhone } from '../utils/callPhone'
import { setSeoMeta } from '../utils/seo'

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
const cases = computed(() => state.cases || [])

const activeBanners = computed(() => {
  const list = Array.isArray(state.banners) ? state.banners : []
  return list
    .filter(b => b && b.isActive !== false)
    .sort((a, b) => (a.sort || 0) - (b.sort || 0))
})

const bannerIndex = ref(0)
const currentBanner = computed(() => activeBanners.value[bannerIndex.value] || null)
const bannerCtaText = computed(() => {
  const linkType = (currentBanner.value?.linkType || '').toLowerCase()
  if (linkType === 'product') return '查看详情'
  if (linkType === 'custom') return '查看'
  return '立即咨询'
})
let bannerTimer = null

function setBannerIndex(i) {
  bannerIndex.value = i
}

function onBannerCtaClick(b) {
  if (!b) return
  // MVP：只处理常见 linkType。custom 里如果是站内路径（以 / 开头），用路由跳转，否则用 location。
  const linkType = (b.linkType || '').toLowerCase()
  const linkValue = b.linkValue || ''

  if (linkType === 'inquiry') {
    router.push('/inquiry')
    return
  }
  if (linkType === 'product') {
    router.push(`/products/${linkValue}`)
    return
  }
  if (linkType === 'custom') {
    if (linkValue.startsWith('/')) {
      const resolved = router.resolve(linkValue)
      if (resolved.matched && resolved.matched.length) {
        router.push(linkValue)
      } else {
        router.push('/contact')
      }
    } else if (linkValue) {
      window.location.href = linkValue
    } else {
      router.push('/contact')
    }
    return
  }

  router.push('/contact')
}

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

    // OG/分享 meta：Home 优先使用后端配置的 SEO
    setSeoMeta({
      title: companyRes?.seoTitle || '农村红木家具广告',
      description: companyRes?.seoDescription || '',
      url: window.location.href,
      ogImage: null
    })

    // 轮播初始化
    if (activeBanners.value.length) {
      bannerIndex.value = 0
      bannerTimer = setInterval(() => {
        if (!activeBanners.value.length) return
        bannerIndex.value = (bannerIndex.value + 1) % activeBanners.value.length
      }, 3500)
    }
  } catch (e) {
    console.error(e)
    alert('加载失败，请稍后再试。')
  } finally {
    state.loading = false
  }
})

onUnmounted(() => {
  if (bannerTimer) {
    clearInterval(bannerTimer)
    bannerTimer = null
  }
})
</script>

