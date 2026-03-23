<template>
  <div>
    <h2 style="margin:0 0 10px 0;font-size:16px;">提交线索（微信咨询兜底）</h2>

    <div style="border:1px solid #eee;border-radius:10px;padding:12px;">
      <div style="margin-bottom:10px;font-size:13px;color:#666;">
        请填写电话，完成图片验证码后提交。提交后我们将尽快联系你。
      </div>

      <div style="display:flex;gap:12px;align-items:flex-end;flex-wrap:wrap;margin-bottom:10px;">
        <div style="flex: 0 0 auto;">
          <img
            v-if="captcha.captchaImage"
            :src="`data:image/png;base64,${captcha.captchaImage}`"
            alt="captcha"
            style="width:160px;height:55px;border:1px solid #eee;border-radius:6px;"
          />
        </div>
        <div style="flex: 1 1 auto; min-width: 180px;">
          <button
            @click="refreshCaptcha"
            style="border:1px solid #e5e7eb;background:#fff;border-radius:8px;padding:8px 12px;font-size:13px;"
          >
            刷新验证码
          </button>
        </div>
      </div>

      <div style="display:flex;flex-direction:column;gap:10px;">
        <label style="font-size:13px;">
          电话（必填）
          <input v-model="form.phone" type="tel" placeholder="输入手机号" style="width:100%;padding:10px;border:1px solid #e5e7eb;border-radius:8px;margin-top:6px;" />
        </label>

        <label style="font-size:13px;">
          图片验证码
          <input v-model="form.captchaCode" placeholder="输入验证码" style="width:100%;padding:10px;border:1px solid #e5e7eb;border-radius:8px;margin-top:6px;" />
        </label>

        <label style="font-size:13px;">
          姓名（可选）
          <input v-model="form.name" placeholder="输入姓名（可选）" style="width:100%;padding:10px;border:1px solid #e5e7eb;border-radius:8px;margin-top:6px;" />
        </label>

        <label style="font-size:13px;">
          所在地区（可选）
          <input v-model="form.region" placeholder="如：XX省XX市（可选）" style="width:100%;padding:10px;border:1px solid #e5e7eb;border-radius:8px;margin-top:6px;" />
        </label>

        <label style="font-size:13px;">
          需求描述（可选）
          <textarea v-model="form.description" placeholder="如：餐桌/柜类/茶台，预算等（可选）" rows="3" style="width:100%;padding:10px;border:1px solid #e5e7eb;border-radius:8px;margin-top:6px;resize:vertical;"></textarea>
        </label>

        <button
          :disabled="submitting"
          @click="submit"
          style="background:#b45309;color:#fff;padding:12px;border-radius:10px;border:none;font-size:14px;"
        >
          {{ submitting ? '提交中...' : '提交' }}
        </button>

        <div v-if="message" :style="message.isError ? 'color:#b91c1c;' : 'color:#065f46;'" style="font-size:13px;">
          {{ message.text }}
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, reactive } from 'vue'
import { getJson, postJson } from '../api/client'

const state = reactive({
  captcha: { captchaId: '', captchaImage: '' },
  submitting: false,
  message: null,
  form: {
    phone: '',
    name: '',
    region: '',
    description: '',
    captchaId: '',
    captchaCode: ''
  }
})

const captcha = computed(() => state.captcha)
const submitting = computed(() => state.submitting)
const form = computed(() => state.form)
const message = computed(() => state.message)

async function refreshCaptcha() {
  const c = await getJson('/api/captcha')
  state.captcha.captchaId = c.captchaId
  state.captcha.captchaImage = c.captchaImage
  state.form.captchaId = c.captchaId
}

onMounted(async () => {
  await refreshCaptcha()
})

async function submit() {
  state.submitting = true
  state.message = null
  try {
    // 注意：后端请求字段名是 captchaId/captchaCode
    const payload = {
      phone: state.form.phone,
      name: state.form.name || '',
      region: state.form.region || '',
      description: state.form.description || '',
      captchaId: state.form.captchaId,
      captchaCode: state.form.captchaCode
    }
    await postJson('/api/inquiries', payload)
    state.message = { isError: false, text: '提交成功，等待联系' }
  } catch (e) {
    const text = e.message || '提交失败'
    state.message = { isError: true, text }

    // 如果验证码过期，按你给的文案提示并自动刷新
    if (text.includes('验证码过期')) {
      await refreshCaptcha()
      state.form.captchaCode = ''
    }
  } finally {
    state.submitting = false
  }
}
</script>

