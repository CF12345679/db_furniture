function upsertMeta({ name, property, content }) {
  if (!content && content !== '') return
  const selector = property
    ? `meta[property="${property}"]`
    : `meta[name="${name}"]`
  let meta = document.head.querySelector(selector)
  if (!meta) {
    meta = document.createElement('meta')
    if (property) meta.setAttribute('property', property)
    if (name) meta.setAttribute('name', name)
    document.head.appendChild(meta)
  }
  meta.setAttribute('content', content)
}

function upsertLinkRel({ rel, href }) {
  if (!href) return
  let link = document.head.querySelector(`link[rel="${rel}"]`)
  if (!link) {
    link = document.createElement('link')
    link.setAttribute('rel', rel)
    document.head.appendChild(link)
  }
  link.setAttribute('href', href)
}

export function svgPlaceholderOgImage(text) {
  const safe = (text || 'dbfurniture').toString()
  const display = safe.length > 14 ? safe.slice(0, 14) + '...' : safe
  const svg = `
<svg xmlns="http://www.w3.org/2000/svg" width="1200" height="630">
  <defs>
    <linearGradient id="g" x1="0" y1="0" x2="1" y2="1">
      <stop offset="0" stop-color="#f97316" stop-opacity="0.95"/>
      <stop offset="1" stop-color="#0f766e" stop-opacity="0.95"/>
    </linearGradient>
  </defs>
  <rect width="1200" height="630" fill="url(#g)"/>
  <rect x="80" y="80" width="1040" height="470" rx="34" fill="rgba(255,255,255,0.86)"/>
  <text x="160" y="300" font-family="Arial" font-size="72" font-weight="700" fill="#0f172a">${escapeXml(
    display
  )}</text>
  <text x="160" y="380" font-family="Arial" font-size="28" font-weight="400" fill="#334155">农村红木家具广告网站</text>
</svg>`
  return `data:image/svg+xml;charset=utf-8,${encodeURIComponent(svg.trim())}`
}

function escapeXml(s) {
  return (s || '')
    .replaceAll('&', '&amp;')
    .replaceAll('<', '&lt;')
    .replaceAll('>', '&gt;')
    .replaceAll('"', '&quot;')
    .replaceAll("'", '&apos;')
}

export function setSeoMeta({ title, description, url, ogImage }) {
  const t = (title || '').toString()
  const d = (description || '').toString()
  const u = url || (typeof window !== 'undefined' ? window.location.href : '')
  const img = ogImage || svgPlaceholderOgImage(title || 'dbfurniture')

  // browser title
  if (t) document.title = t

  // basic description
  upsertMeta({ name: 'description', content: d })

  // OpenGraph tags (WeChat OG)
  upsertMeta({ property: 'og:title', content: t })
  upsertMeta({ property: 'og:description', content: d })
  upsertMeta({ property: 'og:type', content: 'website' })
  upsertMeta({ property: 'og:url', content: u })
  upsertMeta({ property: 'og:image', content: img })

  // canonical (optional)
  if (u) upsertLinkRel({ rel: 'canonical', href: u })
}

