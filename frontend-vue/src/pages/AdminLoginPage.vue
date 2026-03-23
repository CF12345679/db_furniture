<template>
  <div>
    <h2 style="margin:0 0 12px 0;font-size:16px;">管理员登录（MVP）</h2>

    <div style="border:1px solid #eee;border-radius:10px;padding:12px;">
      <div style="font-size:13px;color:#666;margin-bottom:10px;">
        默认账号：<b>admin</b>，密码：<b>admin</b>
      </div>

      <div style="display:flex;flex-direction:column;gap:10px;">
        <label style="font-size:13px;">
          用户名
          <input v-model="form.username" style="width:100%;padding:10px;border:1px solid #e5e7eb;border-radius:8px;margin-top:6px;" />
        </label>
        <label style="font-size:13px;">
          密码
          <input
            v-model="form.password"
            type="password"
            style="width:100%;padding:10px;border:1px solid #e5e7eb;border-radius:8px;margin-top:6px;"
          />
        </label>

        <button
          :disabled="loading"
          @click="login"
          style="background:#0f766e;color:#fff;padding:12px;border-radius:10px;border:none;font-size:14px;"
        >
          {{ loading ? '登录中...' : '登录' }}
        </button>

        <div v-if="message" :style="message.isError ? 'color:#b91c1c;' : 'color:#065f46;'" style="font-size:13px;">
          {{ message.text }}
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { postJson } from '../api/client'

const router = useRouter()
const loading = ref(false)
const message = reactive({ isError: false, text: '' })
const form = reactive({
  username: 'admin',
  password: 'admin'
})

async function login() {
  loading.value = true
  message.text = ''
  message.isError = false
  try {
    const res = await postJson('/api/admin/login', form)
    if (!res || !res.token) throw new Error('登录失败：未返回 token')
    localStorage.setItem('admin_token', res.token)
    message.text = '登录成功'
    router.push('/admin/catalog')
  } catch (e) {
    message.isError = true
    message.text = e.message || '登录失败'
  } finally {
    loading.value = false
  }
}
</script>

