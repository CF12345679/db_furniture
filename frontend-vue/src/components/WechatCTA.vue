<template>
  <div style="margin-top:14px; border:1px solid #eee; border-radius:10px; padding:12px;">
    <div style="font-size:13px;color:#666;margin-bottom:8px;">微信咨询（扫码 + 复制微信号）</div>
    <div style="font-weight:700;margin-bottom:10px;">{{ weChat }}</div>

    <div style="display:flex; gap:14px; align-items:center; flex-wrap:wrap;">
      <div>
        <div v-if="loading" style="font-size:13px;color:#666;">生成二维码中...</div>
        <img
          v-else-if="qrDataUrl"
          :src="qrDataUrl"
          alt="weChat QR"
          style="width:150px; height:150px; border:1px solid #eee; border-radius:8px;"
        />
        <div v-else style="font-size:13px;color:#b91c1c;">二维码暂不可用</div>
      </div>

      <div style="flex:1 1 180px;">
        <div style="margin-bottom:10px; font-size:13px; color:#333; line-height:1.5;">
          1. 手机打开微信
          <br />
          2. 扫码添加（或复制微信号手动添加）
        </div>

        <button
          @click="copyWeChat"
          style="background:#111827; color:#fff; padding:10px 12px; border-radius:8px; border:none; font-size:14px;"
        >
          复制微信号
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, ref, watch } from 'vue'
import QRCode from 'qrcode'

const props = defineProps({
  weChat: { type: String, default: '' }
})

const loading = ref(false)
const qrDataUrl = ref('')

const qrText = computed(() => {
  const id = (props.weChat || '').trim()
  if (!id) return ''
  // 用 weixin://dl/chat 在微信端更可能识别为“添加好友入口”
  return `weixin://dl/chat?wxid=${encodeURIComponent(id)}`
})

async function renderQr() {
  const text = qrText.value
  if (!text) {
    qrDataUrl.value = ''
    return
  }

  loading.value = true
  try {
    qrDataUrl.value = await QRCode.toDataURL(text, { margin: 1, width: 180 })
  } catch (e) {
    qrDataUrl.value = ''
  } finally {
    loading.value = false
  }
}

watch(
  () => props.weChat,
  async () => {
    await renderQr()
  },
  { immediate: true }
)

async function copyWeChat() {
  const id = (props.weChat || '').trim()
  if (!id) {
    alert('暂无微信号')
    return
  }

  if (navigator.clipboard?.writeText) {
    try {
      await navigator.clipboard.writeText(id)
      alert(`微信号已复制：${id}`)
      return
    } catch (e) {
      // fallthrough
    }
  }

  // fallback：老环境使用临时 input
  const input = document.createElement('input')
  input.value = id
  document.body.appendChild(input)
  input.select()
  try {
    document.execCommand('copy')
    alert(`微信号已复制：${id}`)
  } catch (_) {
    alert(`请手动复制微信号：${id}`)
  } finally {
    document.body.removeChild(input)
  }
}
</script>

