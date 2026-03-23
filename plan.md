## 项目计划（农村红木家具广告网站 · 微信端）

### 1. 需求澄清（在开始开发前需要确定）

为确保后续实现一次到位，建议先确认这些关键点（围绕微信 H5 落地）：

1. 微信落地形态二选一：
   - 已选择：A：微信 H5（在微信内打开网页）
2. 门店联系方式：
    - 电话：固定（单门店）
    - 微信联系入口：微信号（可复制添加）+ 微信二维码（扫码添加）
    - 备注：微信咨询优先（添加微信）；同时保留“拨打电话”作为兜底入口
3. 内容规模预估（影响数据库与后台设计）：
    - 产品数量区间：50-200
    - 分类数量区间：8-20
4. 线索处理方式：
   - 管理端查看后手动联系
    - 是否需要自动推送到企业微信/邮箱：不做（MVP 先手动）
5. 你希望的主要获客路径：
    - 微信咨询优先（添加微信）
    - 线索表单保留（作为补充/兜底）
    - 拨打电话：可作为备选入口（兜底）

6.（H5 特定）分享展示方式偏好：
   - A：A1（只用页面 `OG/分享 meta` 做默认分享预览；实现简单）

### 2. 页面与功能清单（MVP -> V1）

#### MVP（第 1 阶段，重点“能展示、能获客、能后台维护”）

访客端（微信可见）：

1. 首页
   - 轮播图（可后台配置）
   - 品牌/门店优势（图文）
   - 特色产品（列表）
   - 案例/评价（列表）
   - 门店信息（电话、地址、导航按钮）
   - CTA 区域（微信咨询 / 拨号 / 提交需求）
2. 产品分类页
   - 分类列表 / 分类筛选
   - 分类下产品列表
3. 产品详情页
   - 图片轮播/缩略图
   - 尺寸/材质/工艺说明（可后台编辑）
   - 关键 CTA（微信咨询/拨号）
4. 线索提交页（表单）
   - 姓名（可选）
   - 电话（必填）
   - 所在地区（可选）
   - 需求描述（可选）
   - 提交成功页
5. 关于我们（品牌故事、服务承诺）
6. 联系我们（电话、地址、地图链接）

管理端：

1. 登录/权限（管理员单用户 MVP）
2. 内容管理
   - 轮播图管理
   - 首页模块开关（简单配置）
   - 分类管理
   - 产品管理（含图片）
   - 案例/评价管理（可审核/不审核待定）
3. 线索管理
   - 线索列表（新建/已联系/忽略）
   - 线索详情（展示提交字段和时间）
4. 基础配置
   - 电话、地址、微信入口、SEO 分享文案

#### V1（第 2 阶段，增强转化与运营能力）

- 用户分享引导与“分享海报样式”（若 H5 走分享卡片可加 SEO/分享配置）
- 增加“活动/套餐/报价单”页面（可选）
- 线索导出、简单统计（来源/数量/转化状态）
- 图片压缩、懒加载与性能优化

### 3. 技术方案建议（实现前确认）

由于你要求微信端展示，技术方案的目标是“移动端友好 + 可维护后台 + 快速加载”。

已确定路线：微信 H5（在微信内打开网页）

1. 前端建议
   - 框架：Next.js / Vue（响应式，适配微信内置浏览器）
   - 关键：`viewport` 适配、页面首屏性能（弱网可用）、图片懒加载、合理的缓存策略
2. 后端建议
   - 技术：Node.js（NestJS/Express）或 Java（Spring Boot）
   - 接口：REST/JSON（用于首页/列表/详情/线索提交/管理端）
3. 数据与资源
   - 数据库：MySQL/PostgreSQL
   - 图片：对象存储（OSS/MinIO）+ 可选 CDN
4. 微信 H5 落地要点（开发时必须考虑）
   - 电话按钮：使用 `tel:` 链接，确保微信内一键拨号可用
   - 分享卡片：为每个页面准备 `title/description/封面图`，通过 `OG` meta 生成默认分享预览
   - HTTPS 与域名：微信内访问必须走 `https`，并确保你的域名可被微信正常访问
   - （可选）如果选择需求澄清第 6 条的 A2：需要公众号后台配置相关“JS 安全域名/网页授权”并引入微信 JS-SDK（仅在需要精细分享参数时启用）

### 4. 数据模型草图（仅作为实现方向）

- `Product`：产品基础信息（标题、分类 ID、工艺/材质说明、图片、排序、是否上架）
- `Category`：产品分类（名称、排序）
- `Banner`：轮播图（标题/链接/图片/排序/启用状态）
- `CaseReview`：案例/评价（图片/文字/是否启用）
- `Inquiry`：线索（`phone` 必填；`name/region/description` 可选；`status`、`createdAt`）
- `CompanyProfile`：门店/品牌信息（电话、地址、微信入口、SEO）

### 5. 阶段计划（建议）

1. 阶段 0：需求冻结与原型确认（1-2 天）
   - 确定微信 H5 入口、域名/落地页路径与分享策略（OG）
   - 确定页面清单、字段字典、样式方向（H5 端以移动端优先）
2. 阶段 1：MVP 开发（3-7 天）
   - 访客端页面 + 管理端最小可用
   - 线索提交链路跑通（提交后能在管理端看到）
3. 阶段 2：V1 增强与部署（2-5 天）
   - 性能优化、分享/SEO 配置
   - 部署到可被微信访问的 `https` 域名，并完成分享卡片（OG）验证
   - 若选择 A2：补齐公众号后台配置并完成 JS-SDK/分享参数联调
4. 阶段 3：测试与交付（1-2 天）
   - 弱网/微信内打开测试
   - 管理端维护流程培训（文档或简单说明）

### 6. 验收标准（可量化）

访客端：

1. 在微信内打开首页与任意产品详情页，页面无明显报错，CTA 可点击。
2. 线索表单提交成功后，在管理端能看到同一条线索记录（时间/字段一致）。
3. 核心页面首屏加载体验可接受（弱网下不出现长时间空白；具体阈值可在你确认内容体量后定）。
4. 适配常见手机分辨率：文字可读、按钮区域足够大、图片自适应不遮挡。

管理端：

1. 管理员可以完成：轮播图上下架、分类增改删（MVP 版）。
2. 管理员可以完成：新增产品并发布，上线后访客端可见。
3. 管理员可以查看线索列表与线索详情，并修改状态。

### 7. 风险与注意事项

1. 微信端展示依赖部署方式：如果选择 H5，需要确保域名/https 可被微信正常访问。
2. 微信分享卡片表现受多因素影响：封面图裁剪、OG 标签更新延迟、以及微信侧缓存；需要在部署后做多次验证。
3. 图片质量与体积会直接影响弱网加载体验：需要计划图片压缩与懒加载。
4. 线索数据隐私与反垃圾：表单需做防刷，使用后端生成的图片验证码验证（并可配合简单限流）。

后端生成图片验证码（MVP 推荐流程）：
- Step 1：获取验证码 `GET /api/captcha`
  - 返回：`captchaId`（用于关联验证码）+ `captchaImage`（建议返回图片 base64 或可直接渲染的图片 URL）
- Step 2：用户填写 `captchaCode` 后提交线索 `POST /api/inquiries`
  - body：`phone + captchaId + captchaCode`（以及可选的 `name/region/description`）
- 校验要点：后端用服务端存储的验证码（或其 hash）校验，验证码有效期 1 分钟；校验成功后可将该 `captchaId` 标记为已使用（可选）
- 异常约定：若 `captchaId` 过期，返回错误信息“验证码过期请重新获取并刷新图片”（前端据此提示并引导用户重新获取验证码）

---
### 8. 访客端页面路由（微信 H5）
- `GET /`：首页
- `GET /categories/:categoryId`：分类列表（分类下产品）
- `GET /products/:productId`：产品详情
- `GET /inquiry`：线索提交表单（可作为“微信咨询失败/用户主动”的兜底）
- `GET /inquiry/success`：提交成功页
- `GET /about`：关于我们
- `GET /contact`：联系/门店信息（电话、地址、微信咨询入口、导航按钮）

### 9. 字段字典（实现前用于定表）
`Category`：分类
- `id`（int/uuid）
- `name`（string，分类名）
- `sort`（int）
- `isActive`（bool）

`Product`：产品
- `id`
- `categoryId`
- `title`
- `summary`（短描述，用于列表）
- `materialsCraft`（工艺/材质/特点，富文本或纯文本）
- `specs`（尺寸等，可结构化 JSON 或纯文本）
- `imageIds`（string 数组，产品多图）
- `isActive`
- `sort`

`Banner`：轮播图
- `id`
- `title`（可选）
- `linkType`（例如 `inquiry`/`product`/`custom`）
- `linkValue`（例如 productId 或 URL）
- `imageId`
- `sort`
- `isActive`

`CaseReview`：案例/评价（可先不做复杂审核流程，MVP 直接展示）
- `id`
- `title`（可选）
- `content`（文字，可选）
- `imageIds`
- `isActive`
- `sort`

`Inquiry`：线索
- `id`
- `name`（可选）
- `phone`（必填，电话）
- `region`（可选，所在地区）
- `description`（可选，需求描述）
- `status`（`NEW`/`CONTACTED`/`IGNORED`）
- `createdAt`
- `updatedAt`

`CompanyProfile`：门店/品牌基础信息
- `id`
- `companyName`（门店/品牌名）
- `phone`（固定电话）
- `address`（地址）
- `weChat`（微信号或客服标识，或用于展示的名称）
- `weChatQrImageId`（微信二维码图片 ID）
- `navLat`（可选）
- `navLng`（可选）
- `seoTitle` / `seoDescription`

`AdminUser`：管理员（MVP 单用户）
- `id`
- `username`
- `passwordHash`
- `createdAt`

`AdminInquiryNote`：线索处理备注（可选）
- `id`
- `inquiryId`
- `note`（文字）
- `createdAt`

### 10. API 草图（REST/JSON，H5 + 管理端）
公共接口（访客端）
- `GET /api/banners`：轮播图列表
- `GET /api/categories`：分类列表
- `GET /api/products?categoryId=...`：分类下产品列表（可支持分页，但 MVP 可先不分页）
- `GET /api/products/:productId`：产品详情
- `GET /api/cases`：案例/评价列表（可选启用）
- `GET /api/company`：门店/品牌信息（含电话、地址、微信二维码展示信息）
- `GET /api/captcha`：获取后端生成的图片验证码（返回 `captchaId` + `captchaImage`）
- `POST /api/inquiries`：提交线索表单（body：`phone + captchaId + captchaCode`；以及可选的 `name/region/description`）

管理接口（管理员端）
- `POST /api/admin/login`：登录
- `GET /api/admin/products`：产品列表
- `POST /api/admin/products`：新增产品
- `PUT /api/admin/products/:id`：编辑产品
- `PATCH /api/admin/products/:id/publish`：上下架（可选）
- `GET /api/admin/categories`、`POST /api/admin/categories`、`PUT /api/admin/categories/:id`
- `GET /api/admin/banners`、`POST /api/admin/banners`、`PUT /api/admin/banners/:id`
- `GET /api/admin/inquiries`：线索列表（按状态筛选可选）
- `PUT /api/admin/inquiries/:id`：修改线索状态（例如 `CONTACTED`）
- `PUT /api/admin/company`：修改门店基础配置与 SEO 分享文案

媒体/上传接口（MVP 可先用现成存储上传流程）
- `POST /api/media/upload`：上传图片并返回 `imageId`

### 11. 素材与配置清单（你需要准备/确认的）
- 门店信息：固定电话、地址（以及如能提供经纬度更佳）
- 微信咨询入口：微信号或客服标识 + 微信二维码（至少一张清晰二维码）
- 图片素材：轮播图（建议 3-5 张）、产品图片（每个产品至少 3 张，含主图/细节图）、案例图片（可选 1-10 张）
- 文案素材：品牌优势要点、工艺/材质说明模板（可编辑）、CTA 文案（“添加微信咨询”优先）
- 分享/SEO：每种落地页的 `title/description` 与 OG 封面图（MVP 可以先统一一套模板，后续再细化）

---
已确认：
- 线索表单保留（MVP 内上线）
- 微信咨询入口：微信号（可复制添加）+ 微信二维码（扫码添加）
- MVP 表单字段：`电话` 必填，`姓名/地区/需求描述` 可选
- 防刷实现：后端生成图片验证码验证（有效期 1 分钟）

