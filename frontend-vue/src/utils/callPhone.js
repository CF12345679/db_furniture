export async function callPhone(phone) {
  const normalized = (phone || '').toString().trim()
  if (!normalized) {
    alert('暂无联系电话')
    return
  }

  const isMobile = /Android|iPhone|iPad|iPod|Mobile/i.test(navigator.userAgent)

  // 手机端通常可以直接触发拨号；桌面端往往不支持，做复制兜底给用户反馈。
  if (isMobile) {
    window.location.href = `tel:${normalized}`
    return
  }

  if (navigator.clipboard?.writeText) {
    try {
      await navigator.clipboard.writeText(normalized)
      alert(`当前设备不支持直接拨号，号码已复制：${normalized}`)
    } catch (_) {
      alert(`当前设备不支持直接拨号，请手动拨打：${normalized}`)
    }
  } else {
    alert(`当前设备不支持直接拨号，请手动拨打：${normalized}`)
  }
}

