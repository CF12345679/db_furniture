<template>
  <div>
    <h2 style="margin:0 0 12px 0;font-size:16px;">线索列表（管理端 · MVP）</h2>

    <div style="margin-bottom:12px; font-size:13px; color:#666;">
      说明：当前先实现“查看与改状态”。后续补登录权限即可。
    </div>

    <div v-if="loading" style="padding:12px 0;">加载中...</div>
    <div v-else>
      <div v-if="!inquiries.length" style="padding:12px;border:1px dashed #ddd;border-radius:10px;color:#666;">
        暂无线索。你可以先到 `咨询` 页面提交一个线索，列表会自动出现。
      </div>

      <div v-else style="border:1px solid #eee;border-radius:10px; overflow:hidden;">
        <div
          v-for="item in inquiries"
          :key="item.id"
          style="padding:12px;border-top:1px solid #f0f0f0;"
        >
          <div style="display:flex; justify-content:space-between; gap:12px; align-items:flex-start; flex-wrap:wrap;">
            <div style="min-width:200px;">
              <div style="font-weight:700;margin-bottom:6px;">电话：{{ item.phone }}</div>
              <div style="font-size:12px;color:#666;margin-bottom:4px;">地区：{{ item.region || '-' }}</div>
              <div style="font-size:12px;color:#666;margin-bottom:4px;">提交时间：{{ formatTime(item.createdAt) }}</div>
              <div style="font-size:13px;color:#333; white-space:pre-wrap;">需求：{{ item.description || '-' }}</div>
            </div>

            <div style="flex:1; min-width:220px;">
              <div style="font-size:12px;color:#666;margin-bottom:8px;">当前状态：{{ item.status }}</div>
              <div style="display:flex; gap:8px; flex-wrap:wrap;">
                <button
                  @click="update(item.id, 'CONTACTED')"
                  style="background:#0f766e;color:#fff;border:none;border-radius:8px;padding:8px 10px;font-size:13px;"
                >
                  标记已联系
                </button>
                <button
                  @click="update(item.id, 'IGNORED')"
                  style="background:#6b7280;color:#fff;border:none;border-radius:8px;padding:8px 10px;font-size:13px;"
                >
                  忽略
                </button>
                <button
                  @click="update(item.id, 'NEW')"
                  style="background:#fff;color:#111827;border:1px solid #e5e7eb;border-radius:8px;padding:8px 10px;font-size:13px;"
                >
                  重置为新建
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div v-if="toast" style="margin-top:12px;font-size:13px;color:#065f46;">
      {{ toast }}
    </div>
    <div v-if="toastError" style="margin-top:12px;font-size:13px;color:#b91c1c;">
      {{ toastError }}
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, reactive } from 'vue'
import { getJson, putJson } from '../api/client'

const state = reactive({
  loading: true,
  inquiries: [],
  toast: '',
  toastError: ''
})

const adminToken = localStorage.getItem('admin_token') || ''

const loading = computed(() => state.loading)
const inquiries = computed(() => state.inquiries)
const toast = computed(() => state.toast)
const toastError = computed(() => state.toastError)

function formatTime(t) {
  if (!t) return '-'
  const d = new Date(t)
  if (Number.isNaN(d.getTime())) return '-'
  return d.toLocaleString()
}

async function refresh() {
  state.toast = ''
  state.toastError = ''
  state.loading = true
  try {
    if (!adminToken) throw new Error('未登录，请先到管理员登录页')
    const list = await getJson('/api/admin/inquiries', { headers: { 'X-Admin-Token': adminToken } })
    state.inquiries = Array.isArray(list) ? list : []
  } catch (e) {
    state.toastError = e.message || '加载失败'
  } finally {
    state.loading = false
  }
}

async function update(id, status) {
  state.toast = ''
  state.toastError = ''
  try {
    if (!adminToken) throw new Error('未登录，请先到管理员登录页')
    await putJson(
      `/api/admin/inquiries/${encodeURIComponent(id)}`,
      { status },
      { headers: { 'X-Admin-Token': adminToken } }
    )
    state.toast = '更新成功'
    await refresh()
  } catch (e) {
    state.toastError = e.message || '更新失败'
  }
}

onMounted(() => refresh())
</script>

