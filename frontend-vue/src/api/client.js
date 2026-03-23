export async function getJson(url) {
  const res = await fetch(url, { method: 'GET' })
  if (!res.ok) {
    throw new Error(`GET ${url} failed: ${res.status}`)
  }
  return res.json()
}

export async function postJson(url, body) {
  const res = await fetch(url, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(body)
  })

  // 失败时也尽量返回 JSON 以便显示后端 message
  const text = await res.text()
  const json = text ? safeJsonParse(text) : null

  if (!res.ok) {
    const message = (json && (json.message || json.error)) || `POST ${url} failed: ${res.status}`
    const err = new Error(message)
    err.status = res.status
    err.payload = json
    throw err
  }
  return json
}

export async function putJson(url, body) {
  const res = await fetch(url, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(body)
  })

  const text = await res.text()
  const json = text ? safeJsonParse(text) : null

  if (!res.ok) {
    const message = (json && (json.message || json.error)) || `PUT ${url} failed: ${res.status}`
    const err = new Error(message)
    err.status = res.status
    err.payload = json
    throw err
  }

  return json
}

function safeJsonParse(text) {
  try {
    return JSON.parse(text)
  } catch {
    return null
  }
}

