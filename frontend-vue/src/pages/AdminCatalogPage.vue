<template>
  <div>
    <h2 style="margin:0 0 12px 0;font-size:16px;">管理端内容管理（MVP）</h2>

    <div style="margin-bottom:12px;font-size:13px;color:#666;">
      说明：当前为内存数据版，重启后会恢复占位数据。请先尽快把页面 CTA 与线索链路闭环跑通。
    </div>

    <div style="margin-bottom:12px; display:flex; gap:10px; flex-wrap:wrap;">
      <button @click="logout" style="background:#6b7280;color:#fff;padding:10px 12px;border-radius:8px;border:none;font-size:14px;">
        退出登录
      </button>
      <RouterLink to="/admin/inquiries" style="background:#0f766e;color:#fff;padding:10px 12px;border-radius:8px;text-decoration:none;font-size:14px;">
        线索管理
      </RouterLink>
    </div>

    <div v-if="!token" style="border:1px solid #eee;border-radius:10px;padding:12px;font-size:13px;color:#b91c1c;">
      未登录，请先去 <RouterLink to="/admin/login">管理员登录</RouterLink>。
    </div>

    <div v-else>
      <div v-if="loading" style="padding:12px 0;">加载中...</div>
      <div v-else>
        <div style="display:flex; gap:10px; flex-wrap:wrap; margin-bottom:14px;">
          <button
            @click="activeTab = 'banners'"
            :style="activeTab === 'banners' ? 'background:#0f766e;color:#fff;' : 'background:#fff;color:#111827;border:1px solid #e5e7eb;' + 'padding:10px 12px;border-radius:8px;border:none;font-size:14px;'"
          >
            轮播图
          </button>
          <button
            @click="activeTab = 'categories'"
            :style="activeTab === 'categories' ? 'background:#0f766e;color:#fff;' : 'background:#fff;color:#111827;border:1px solid #e5e7eb;' + 'padding:10px 12px;border-radius:8px;border:none;font-size:14px;'"
          >
            分类
          </button>
          <button
            @click="activeTab = 'products'"
            :style="activeTab === 'products' ? 'background:#0f766e;color:#fff;' : 'background:#fff;color:#111827;border:1px solid #e5e7eb;' + 'padding:10px 12px;border-radius:8px;border:none;font-size:14px;'"
          >
            产品
          </button>
          <button
            @click="activeTab = 'company'"
            :style="activeTab === 'company' ? 'background:#0f766e;color:#fff;' : 'background:#fff;color:#111827;border:1px solid #e5e7eb;' + 'padding:10px 12px;border-radius:8px;border:none;font-size:14px;'"
          >
            门店配置
          </button>
        </div>

        <!-- Banners -->
        <section v-if="activeTab === 'banners'" style="margin-bottom:18px;">
          <h3 style="margin:0 0 10px 0;font-size:14px;">轮播图管理</h3>
          <div style="border:1px dashed #ddd;border-radius:10px;padding:12px;margin-bottom:12px;">
            <div style="display:flex;flex-wrap:wrap;gap:10px;align-items:flex-end;">
              <label style="flex: 1 1 280px; font-size:13px;">
                标题关键词
                <input v-model="bannerFilters.title" placeholder="输入关键词" style="width:100%;padding:10px;border:1px solid #e5e7eb;border-radius:8px;margin-top:6px;" />
              </label>
              <label style="flex: 0 0 140px; font-size:13px;">
                linkType
                <input v-model="bannerFilters.linkType" placeholder="如 inquiry/custom" style="width:100%;padding:10px;border:1px solid #e5e7eb;border-radius:8px;margin-top:6px;" />
              </label>
              <label style="flex: 0 0 120px; font-size:13px;">
                每页行数
                <select v-model.number="bannerPageSize" style="width:100%;padding:10px;border:1px solid #e5e7eb;border-radius:8px;margin-top:6px;">
                  <option :value="10">10</option>
                  <option :value="20">20</option>
                </select>
              </label>
              <button
                @click="openCreateBannerModal"
                style="background:#111827;color:#fff;padding:10px 12px;border-radius:8px;border:none;font-size:14px; flex: 0 0 auto;"
              >
                新增轮播图
              </button>
            </div>
          </div>

          <div style="border:1px solid #eee;border-radius:10px; overflow:hidden;">
            <div style="display:flex;gap:10px; padding:10px; background:#fafafa; font-size:12px; color:#666; align-items:center; flex-wrap:wrap;">
              <div style="flex:2 1 240px;">标题</div>
              <div style="flex:1 1 160px;">linkType</div>
              <div style="flex:1 1 220px;">linkValue</div>
              <div style="flex:0 0 70px;">排序</div>
              <div style="flex:0 0 70px;">启用</div>
              <div style="flex:0 0 140px; text-align:right;">操作</div>
            </div>

            <div v-if="filteredBanners.length === 0" style="padding:16px;color:#666;font-size:13px;">
              未找到轮播图
            </div>

            <div v-else>
              <div
                v-for="b in bannerRows"
                :key="b.id"
                style="display:flex; gap:10px; padding:10px; border-top:1px solid #f0f0f0; align-items:center; flex-wrap:wrap;"
              >
                <div style="flex:2 1 240px; font-size:13px; color:#111827; font-weight:700; word-break:break-word;">
                  {{ b.title }}
                </div>
                <div style="flex:1 1 160px; font-size:13px; color:#374151;">
                  {{ b.linkType }}
                </div>
                <div style="flex:1 1 220px; font-size:13px; color:#374151; word-break:break-word;">
                  {{ b.linkValue }}
                </div>
                <div style="flex:0 0 70px; font-size:13px; color:#374151;">
                  {{ b.sort }}
                </div>
                <div style="flex:0 0 70px; font-size:13px; color:#374151;">
                  {{ b.isActive ? '是' : '否' }}
                </div>
                <div style="flex:0 0 140px; text-align:right;">
                  <button
                    @click="openEditBannerModal(b)"
                    style="background:#0f766e;color:#fff;border:none;border-radius:8px;padding:8px 10px;font-size:13px;"
                  >
                    修改
                  </button>
                  <button
                    @click="deleteBanner(b)"
                    style="background:#b91c1c;color:#fff;border:none;border-radius:8px;padding:8px 10px;font-size:13px; margin-left:8px;"
                  >
                    删除
                  </button>
                </div>
              </div>
            </div>
          </div>

          <div style="margin-top:12px; display:flex; justify-content:center; gap:12px; align-items:center; flex-wrap:wrap;">
            <button
              @click="bannerPrevPage"
              :disabled="bannerPage <= 1"
              style="background:#fff;color:#111827;border:1px solid #e5e7eb;border-radius:8px;padding:10px 12px;font-size:14px;"
            >
              上一页
            </button>
            <div style="font-size:13px;color:#666;">
              第 {{ bannerPage }} 页 / 共 {{ bannerTotalPages }} 页
            </div>
            <button
              @click="bannerNextPage"
              :disabled="bannerPage >= bannerTotalPages"
              style="background:#fff;color:#111827;border:1px solid #e5e7eb;border-radius:8px;padding:10px 12px;font-size:14px;"
            >
              下一页
            </button>
          </div>

          <!-- Banner modal -->
          <div
            v-if="bannerModalOpen"
            style="position:fixed; inset:0; background:rgba(0,0,0,0.45); display:flex; align-items:center; justify-content:center; z-index:100;"
          >
            <div style="width:min(720px, 95vw); background:#fff; border-radius:12px; padding:16px; max-height:85vh; overflow:auto;">
              <h3 style="margin:0 0 12px 0;font-size:16px;">
                {{ bannerModalMode === 'create' ? '新增轮播图' : '修改轮播图' }}
              </h3>

              <div style="display:flex; flex-wrap:wrap; gap:10px;">
                <label style="flex: 1 1 220px; font-size:13px;">
                  标题
                  <input v-model="newBanner.title" style="width:100%;padding:10px;border:1px solid #e5e7eb;border-radius:8px;margin-top:6px;" />
                </label>
                <label style="flex: 1 1 160px; font-size:13px;">
                  linkType
                  <input v-model="newBanner.linkType" style="width:100%;padding:10px;border:1px solid #e5e7eb;border-radius:8px;margin-top:6px;" />
                </label>
                <label style="flex: 1 1 240px; font-size:13px;">
                  linkValue
                  <input v-model="newBanner.linkValue" style="width:100%;padding:10px;border:1px solid #e5e7eb;border-radius:8px;margin-top:6px;" />
                </label>
                <label style="flex: 0 0 120px; font-size:13px;">
                  sort
                  <input v-model.number="newBanner.sort" type="number" style="width:100%;padding:10px;border:1px solid #e5e7eb;border-radius:8px;margin-top:6px;" />
                </label>
                <label style="flex: 0 0 auto; font-size:13px; display:flex; align-items:flex-end; gap:8px; padding-bottom:6px;">
                  <input v-model="newBanner.isActive" type="checkbox" />
                  启用
                </label>
              </div>

              <label style="display:block; font-size:13px; margin-top:10px;">
                imageId
                <input v-model="newBanner.imageId" style="width:100%;padding:10px;border:1px solid #e5e7eb;border-radius:8px;margin-top:6px;" />
              </label>

              <div style="margin-top:14px; display:flex; gap:10px; justify-content:flex-end; flex-wrap:wrap;">
                <button
                  @click="bannerModalOpen = false"
                  style="background:#fff;color:#111827;border:1px solid #e5e7eb;border-radius:8px;padding:10px 12px;font-size:14px;"
                >
                  取消
                </button>
                <button
                  @click="submitBannerModal"
                  style="background:#0f766e;color:#fff;border:none;border-radius:8px;padding:10px 12px;font-size:14px;"
                >
                  {{ bannerModalMode === 'create' ? '创建' : '保存' }}
                </button>
              </div>
            </div>
          </div>
        </section>

        <!-- Categories -->
        <section v-if="activeTab === 'categories'" style="margin-bottom:18px;">
          <h3 style="margin:0 0 10px 0;font-size:14px;">分类管理</h3>
          <div style="border:1px dashed #ddd;border-radius:10px;padding:12px;margin-bottom:12px;">
            <div style="display:flex;flex-wrap:wrap;gap:10px;align-items:flex-end;">
              <label style="flex: 1 1 240px; font-size:13px;">
                分类名称关键词
                <input v-model="categoryFilters.name" placeholder="输入关键词" style="width:100%;padding:10px;border:1px solid #e5e7eb;border-radius:8px;margin-top:6px;" />
              </label>
              <label style="flex: 0 0 120px; font-size:13px;">
                每页行数
                <select v-model.number="categoryPageSize" style="width:100%;padding:10px;border:1px solid #e5e7eb;border-radius:8px;margin-top:6px;">
                  <option :value="10">10</option>
                  <option :value="20">20</option>
                </select>
              </label>
              <button
                @click="openCreateCategoryModal"
                style="background:#111827;color:#fff;padding:10px 12px;border-radius:8px;border:none;font-size:14px; flex: 0 0 auto;"
              >
                新增分类
              </button>
            </div>
          </div>

          <div style="border:1px solid #eee;border-radius:10px; overflow:hidden;">
            <div style="display:flex;gap:10px; padding:10px; background:#fafafa; font-size:12px; color:#666; align-items:center; flex-wrap:wrap;">
              <div style="flex:2 1 240px;">名称</div>
              <div style="flex:0 0 100px;">排序</div>
              <div style="flex:0 0 100px;">启用</div>
              <div style="flex:0 0 160px; text-align:right;">操作</div>
            </div>

            <div v-if="filteredCategories.length === 0" style="padding:16px;color:#666;font-size:13px;">
              未找到分类
            </div>

            <div v-else>
              <div
                v-for="c in categoryRows"
                :key="c.id"
                style="display:flex; gap:10px; padding:10px; border-top:1px solid #f0f0f0; align-items:center; flex-wrap:wrap;"
              >
                <div style="flex:2 1 240px; font-size:13px; color:#111827; font-weight:700; word-break:break-word;">
                  {{ c.name }}
                </div>
                <div style="flex:0 0 100px; font-size:13px; color:#374151;">
                  {{ c.sort }}
                </div>
                <div style="flex:0 0 100px; font-size:13px; color:#374151;">
                  {{ c.isActive ? '是' : '否' }}
                </div>
                <div style="flex:0 0 160px; text-align:right;">
                  <button
                    @click="openEditCategoryModal(c)"
                    style="background:#0f766e;color:#fff;border:none;border-radius:8px;padding:8px 10px;font-size:13px;"
                  >
                    修改
                  </button>
                  <button
                    @click="deleteCategory(c)"
                    style="background:#b91c1c;color:#fff;border:none;border-radius:8px;padding:8px 10px;font-size:13px; margin-left:8px;"
                  >
                    删除
                  </button>
                </div>
              </div>
            </div>
          </div>

          <div style="margin-top:12px; display:flex; justify-content:center; gap:12px; align-items:center; flex-wrap:wrap;">
            <button
              @click="categoryPrevPage"
              :disabled="categoryPage <= 1"
              style="background:#fff;color:#111827;border:1px solid #e5e7eb;border-radius:8px;padding:10px 12px;font-size:14px;"
            >
              上一页
            </button>
            <div style="font-size:13px;color:#666;">
              第 {{ categoryPage }} 页 / 共 {{ categoryTotalPages }} 页
            </div>
            <button
              @click="categoryNextPage"
              :disabled="categoryPage >= categoryTotalPages"
              style="background:#fff;color:#111827;border:1px solid #e5e7eb;border-radius:8px;padding:10px 12px;font-size:14px;"
            >
              下一页
            </button>
          </div>

          <!-- Category modal -->
          <div
            v-if="categoryModalOpen"
            style="position:fixed; inset:0; background:rgba(0,0,0,0.45); display:flex; align-items:center; justify-content:center; z-index:100;"
          >
            <div style="width:min(620px, 95vw); background:#fff; border-radius:12px; padding:16px; max-height:85vh; overflow:auto;">
              <h3 style="margin:0 0 12px 0;font-size:16px;">
                {{ categoryModalMode === 'create' ? '新增分类' : '修改分类' }}
              </h3>

              <label style="display:block; font-size:13px; margin-top:10px;">
                名称
                <input v-model="newCategory.name" style="width:100%;padding:10px;border:1px solid #e5e7eb;border-radius:8px;margin-top:6px;" />
              </label>

              <label style="display:block; font-size:13px; margin-top:10px;">
                sort
                <input v-model.number="newCategory.sort" type="number" style="width:100%;padding:10px;border:1px solid #e5e7eb;border-radius:8px;margin-top:6px;" />
              </label>

              <label style="display:flex; align-items:center; gap:10px; margin-top:12px; font-size:13px;">
                <input v-model="newCategory.isActive" type="checkbox" />
                启用
              </label>

              <div style="margin-top:14px; display:flex; gap:10px; justify-content:flex-end; flex-wrap:wrap;">
                <button
                  @click="categoryModalOpen = false"
                  style="background:#fff;color:#111827;border:1px solid #e5e7eb;border-radius:8px;padding:10px 12px;font-size:14px;"
                >
                  取消
                </button>
                <button
                  @click="submitCategoryModal"
                  style="background:#0f766e;color:#fff;border:none;border-radius:8px;padding:10px 12px;font-size:14px;"
                >
                  {{ categoryModalMode === 'create' ? '创建' : '保存' }}
                </button>
              </div>
            </div>
          </div>
        </section>

        <!-- Products -->
        <section v-if="activeTab === 'products'" style="margin-bottom:18px;">
          <h3 style="margin:0 0 10px 0;font-size:14px;">产品管理</h3>
          <div style="border:1px dashed #ddd;border-radius:10px;padding:12px;margin-bottom:12px;">
            <div style="display:flex;flex-wrap:wrap;gap:10px;align-items:flex-end;">
              <label style="flex: 0 0 220px; font-size:13px;">
                分类筛选
                <select v-model="productFilters.categoryId" style="width:100%;padding:10px;border:1px solid #e5e7eb;border-radius:8px;margin-top:6px;">
                  <option value="">全部</option>
                  <option v-for="c in categories" :key="c.id" :value="c.id">{{ c.name }}</option>
                </select>
              </label>

              <label style="flex: 1 1 240px; font-size:13px;">
                标题关键词
                <input v-model="productFilters.title" placeholder="输入关键词" style="width:100%;padding:10px;border:1px solid #e5e7eb;border-radius:8px;margin-top:6px;" />
              </label>

              <label style="flex: 0 0 120px; font-size:13px;">
                每页行数
                <select v-model.number="productPageSize" style="width:100%;padding:10px;border:1px solid #e5e7eb;border-radius:8px;margin-top:6px;">
                  <option :value="10">10</option>
                  <option :value="20">20</option>
                </select>
              </label>

              <button
                @click="openCreateProductModal"
                style="background:#111827;color:#fff;padding:10px 12px;border-radius:8px;border:none;font-size:14px; flex: 0 0 auto;"
              >
                新增产品
              </button>
            </div>
          </div>

          <div style="border:1px solid #eee;border-radius:10px; overflow:hidden;">
            <div style="display:flex;gap:10px; padding:10px; background:#fafafa; font-size:12px; color:#666; align-items:center;">
              <div style="flex:2 1 240px;">标题</div>
              <div style="flex:1 1 180px;">分类</div>
              <div style="flex:0 0 70px;">排序</div>
              <div style="flex:0 0 70px;">启用</div>
              <div style="flex:0 0 120px; text-align:right;">操作</div>
            </div>

            <div v-if="filteredProducts.length === 0" style="padding:16px;color:#666;font-size:13px;">
              未找到产品
            </div>

            <div v-else>
              <div
                v-for="p in productRows"
                :key="p.id"
                style="display:flex; gap:10px; padding:10px; border-top:1px solid #f0f0f0; align-items:center; flex-wrap:wrap;"
              >
                <div style="flex:2 1 240px; font-size:13px; color:#111827; font-weight:700; word-break:break-word;">
                  {{ p.title }}
                </div>
                <div style="flex:1 1 180px; font-size:13px; color:#374151;">
                  {{ categories.find(c => c.id === p.categoryId)?.name || '-' }}
                </div>
                <div style="flex:0 0 70px; font-size:13px; color:#374151;">
                  {{ p.sort }}
                </div>
                <div style="flex:0 0 70px; font-size:13px; color:#374151;">
                  {{ p.isActive ? '是' : '否' }}
                </div>
                <div style="flex:0 0 120px; text-align:right;">
                  <button
                    @click="openEditProductModal(p)"
                    style="background:#0f766e;color:#fff;border:none;border-radius:8px;padding:8px 10px;font-size:13px;"
                  >
                    修改
                  </button>
                  <button
                    @click="deleteProduct(p)"
                    style="background:#b91c1c;color:#fff;border:none;border-radius:8px;padding:8px 10px;font-size:13px; margin-left:8px;"
                  >
                    删除
                  </button>
                </div>
              </div>
            </div>
          </div>

          <div style="margin-top:12px; display:flex; justify-content:center; gap:12px; align-items:center; flex-wrap:wrap;">
            <button
              @click="productPrevPage"
              :disabled="productPage <= 1"
              style="background:#fff;color:#111827;border:1px solid #e5e7eb;border-radius:8px;padding:10px 12px;font-size:14px;"
            >
              上一页
            </button>
            <div style="font-size:13px;color:#666;">
              第 {{ productPage }} 页 / 共 {{ productTotalPages }} 页
            </div>
            <button
              @click="productNextPage"
              :disabled="productPage >= productTotalPages"
              style="background:#fff;color:#111827;border:1px solid #e5e7eb;border-radius:8px;padding:10px 12px;font-size:14px;"
            >
              下一页
            </button>
          </div>

          <!-- Product modal -->
          <div
            v-if="productModalOpen"
            style="position:fixed; inset:0; background:rgba(0,0,0,0.45); display:flex; align-items:center; justify-content:center; z-index:100;"
          >
            <div style="width:min(720px, 95vw); background:#fff; border-radius:12px; padding:16px; max-height:85vh; overflow:auto;">
              <h3 style="margin:0 0 12px 0;font-size:16px;">
                {{ productModalMode === 'create' ? '新增产品' : '修改产品' }}
              </h3>

              <div style="display:flex; flex-wrap:wrap; gap:10px;">
                <label style="flex: 0 0 220px; font-size:13px;">
                  分类
                  <select v-model="newProduct.categoryId" style="width:100%;padding:10px;border:1px solid #e5e7eb;border-radius:8px;margin-top:6px;">
                    <option v-for="c in categories" :key="c.id" :value="c.id">{{ c.name }}</option>
                  </select>
                </label>
                <label style="flex: 1 1 220px; font-size:13px;">
                  标题
                  <input v-model="newProduct.title" style="width:100%;padding:10px;border:1px solid #e5e7eb;border-radius:8px;margin-top:6px;" />
                </label>
                <label style="flex: 1 1 220px; font-size:13px;">
                  summary
                  <input v-model="newProduct.summary" style="width:100%;padding:10px;border:1px solid #e5e7eb;border-radius:8px;margin-top:6px;" />
                </label>
                <label style="flex: 0 0 120px; font-size:13px;">
                  sort
                  <input v-model.number="newProduct.sort" type="number" style="width:100%;padding:10px;border:1px solid #e5e7eb;border-radius:8px;margin-top:6px;" />
                </label>
                <label style="flex: 0 0 auto; font-size:13px; display:flex; align-items:flex-end; gap:8px; padding-bottom:6px;">
                  <input v-model="newProduct.isActive" type="checkbox" />
                  启用
                </label>
              </div>

              <label style="display:block; font-size:13px; margin-top:10px;">
                工艺/材质特点
                <textarea v-model="newProduct.materialsCraft" rows="3" style="width:100%;padding:10px;border:1px solid #e5e7eb;border-radius:8px;margin-top:6px;resize:vertical;"></textarea>
              </label>

              <label style="display:block; font-size:13px; margin-top:10px;">
                尺寸/规格
                <textarea v-model="newProduct.specs" rows="3" style="width:100%;padding:10px;border:1px solid #e5e7eb;border-radius:8px;margin-top:6px;resize:vertical;"></textarea>
              </label>

              <label style="display:block; font-size:13px; margin-top:10px;">
                图片 imageIds（用逗号分隔）
                <input v-model="newProduct._imageIdsText" style="width:100%;padding:10px;border:1px solid #e5e7eb;border-radius:8px;margin-top:6px;" />
              </label>

              <div style="margin-top:14px; display:flex; gap:10px; justify-content:flex-end; flex-wrap:wrap;">
                <button
                  @click="productModalOpen = false"
                  style="background:#fff;color:#111827;border:1px solid #e5e7eb;border-radius:8px;padding:10px 12px;font-size:14px;"
                >
                  取消
                </button>
                <button
                  @click="submitProductModal"
                  style="background:#0f766e;color:#fff;border:none;border-radius:8px;padding:10px 12px;font-size:14px;"
                >
                  {{ productModalMode === 'create' ? '创建' : '保存' }}
                </button>
              </div>
            </div>
          </div>
        </section>

        <!-- Company -->
        <section v-if="activeTab === 'company'">
          <h3 style="margin:0 0 10px 0;font-size:14px;">门店基础配置</h3>
          <div style="border:1px solid #eee;border-radius:10px;padding:12px;">
            <label style="display:block; font-size:13px;">
              门店名称
              <input v-model="company.companyName" style="width:100%;padding:10px;border:1px solid #e5e7eb;border-radius:8px;margin-top:6px;" />
            </label>
            <label style="display:block; font-size:13px; margin-top:10px;">
              电话
              <input v-model="company.phone" style="width:100%;padding:10px;border:1px solid #e5e7eb;border-radius:8px;margin-top:6px;" />
            </label>
            <label style="display:block; font-size:13px; margin-top:10px;">
              地址
              <input v-model="company.address" style="width:100%;padding:10px;border:1px solid #e5e7eb;border-radius:8px;margin-top:6px;" />
            </label>
            <label style="display:block; font-size:13px; margin-top:10px;">
              微信号
              <input v-model="company.weChat" style="width:100%;padding:10px;border:1px solid #e5e7eb;border-radius:8px;margin-top:6px;" />
            </label>

            <button @click="saveCompany" style="margin-top:12px;background:#0f766e;color:#fff;padding:10px 12px;border-radius:8px;border:none;font-size:14px;">
              保存配置
            </button>
          </div>
        </section>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, onMounted, reactive, ref, watch } from 'vue'
import { getJson, postJson, putJson } from '../api/client'
import { useRouter } from 'vue-router'

const router = useRouter()
const token = ref(localStorage.getItem('admin_token') || '')
const loading = ref(false)
const activeTab = ref('banners')

const state = reactive({
  banners: [],
  categories: [],
  products: [],
  company: {}
})

const banners = computed(() => state.banners)
const categories = computed(() => state.categories)
const products = computed(() => state.products)
const company = computed(() => state.company)

const headers = computed(() => ({ 'X-Admin-Token': token.value }))

function normalizeImageIds(text) {
  return (text || '')
    .split(',')
    .map(s => s.trim())
    .filter(Boolean)
}

async function refreshAll() {
  const [bannersRes, categoriesRes, productsRes, companyRes] = await Promise.all([
    getJson('/api/admin/banners', { headers: headers.value }),
    getJson('/api/admin/categories', { headers: headers.value }),
    getJson('/api/admin/products', { headers: headers.value }),
    getJson('/api/admin/company', { headers: headers.value })
  ])

  state.banners = Array.isArray(bannersRes) ? bannersRes : []
  state.categories = Array.isArray(categoriesRes) ? categoriesRes : []

  // 给每个产品补一个 UI 使用的 imageIds 文本字段
  state.products = Array.isArray(productsRes)
    ? productsRes.map(p => ({
        ...p,
        _imageIdsText: Array.isArray(p.imageIds) ? p.imageIds.join(',') : ''
      }))
    : []

  state.company = companyRes || {}

  // 新建产品的默认 categoryId
  if (categories.value.length && (!newProduct.categoryId || !categories.value.find(c => c.id === newProduct.categoryId))) {
    newProduct.categoryId = categories.value[0].id
  }
}

// new forms
const newBanner = reactive({
  title: '',
  linkType: 'inquiry',
  linkValue: '',
  imageId: '',
  sort: 1,
  isActive: true
})

// -----------------------------
// Banners: filters + pagination + modal
// -----------------------------
const bannerFilters = reactive({
  title: '',
  linkType: ''
})

const bannerPageSize = ref(10)
const bannerPage = ref(1)

const filteredBanners = computed(() => {
  const title = (bannerFilters.title || '').trim().toLowerCase()
  const linkType = (bannerFilters.linkType || '').trim()
  return banners.value.filter(b => {
    const okTitle = !title || (b.title || '').toLowerCase().includes(title)
    const okLink = !linkType || (b.linkType || '') === linkType
    return okTitle && okLink
  })
})

const bannerTotalPages = computed(() => {
  const total = filteredBanners.value.length
  const per = bannerPageSize.value || 10
  return Math.max(1, Math.ceil(total / per))
})

const bannerRows = computed(() => {
  const per = bannerPageSize.value || 10
  const start = (bannerPage.value - 1) * per
  return filteredBanners.value.slice(start, start + per)
})

watch(
  () => [bannerFilters.title, bannerFilters.linkType, bannerPageSize.value],
  () => {
    bannerPage.value = 1
  }
)

watch(bannerPage, (v) => {
  if (v < 1) bannerPage.value = 1
  if (v > bannerTotalPages.value) bannerPage.value = bannerTotalPages.value
})

const bannerModalOpen = ref(false)
const bannerModalMode = ref('create') // 'create' | 'edit'
const editingBannerId = ref('')

function resetBannerFormForCreate() {
  newBanner.title = ''
  newBanner.linkType = 'inquiry'
  newBanner.linkValue = ''
  newBanner.imageId = ''
  newBanner.sort = 1
  newBanner.isActive = true
}

function openCreateBannerModal() {
  bannerModalMode.value = 'create'
  editingBannerId.value = ''
  resetBannerFormForCreate()
  bannerModalOpen.value = true
}

function openEditBannerModal(b) {
  if (!b) return
  bannerModalMode.value = 'edit'
  editingBannerId.value = b.id
  newBanner.title = b.title || ''
  newBanner.linkType = b.linkType || 'custom'
  newBanner.linkValue = b.linkValue || ''
  newBanner.imageId = b.imageId || ''
  newBanner.sort = typeof b.sort === 'number' ? b.sort : 1
  newBanner.isActive = !!b.isActive
  bannerModalOpen.value = true
}

function bannerPrevPage() {
  bannerPage.value = Math.max(1, bannerPage.value - 1)
}

function bannerNextPage() {
  bannerPage.value = Math.min(bannerTotalPages.value, bannerPage.value + 1)
}

async function submitBannerModal() {
  const payload = {
    title: newBanner.title,
    linkType: newBanner.linkType,
    linkValue: newBanner.linkValue,
    imageId: newBanner.imageId,
    sort: newBanner.sort,
    isActive: newBanner.isActive
  }

  if (bannerModalMode.value === 'create') {
    await postJson('/api/admin/banners', payload, { headers: headers.value })
  } else {
    await putJson(
      `/api/admin/banners/${encodeURIComponent(editingBannerId.value)}`,
      payload,
      { headers: headers.value }
    )
  }

  bannerModalOpen.value = false
  await refreshAll()
}

const newCategory = reactive({
  name: '',
  sort: 1,
  isActive: true
})

// -----------------------------
// Categories: filters + pagination + modal
// -----------------------------
const categoryFilters = reactive({
  name: ''
})

const categoryPageSize = ref(10)
const categoryPage = ref(1)

const filteredCategories = computed(() => {
  const name = (categoryFilters.name || '').trim().toLowerCase()
  return categories.value.filter(c => {
    if (!name) return true
    return (c.name || '').toLowerCase().includes(name)
  })
})

const categoryTotalPages = computed(() => {
  const total = filteredCategories.value.length
  const per = categoryPageSize.value || 10
  return Math.max(1, Math.ceil(total / per))
})

const categoryRows = computed(() => {
  const per = categoryPageSize.value || 10
  const start = (categoryPage.value - 1) * per
  return filteredCategories.value.slice(start, start + per)
})

watch(
  () => [categoryFilters.name, categoryPageSize.value],
  () => {
    categoryPage.value = 1
  }
)

watch(categoryPage, (v) => {
  if (v < 1) categoryPage.value = 1
  if (v > categoryTotalPages.value) categoryPage.value = categoryTotalPages.value
})

const categoryModalOpen = ref(false)
const categoryModalMode = ref('create') // 'create' | 'edit'
const editingCategoryId = ref('')

function resetCategoryFormForCreate() {
  newCategory.name = ''
  newCategory.sort = 1
  newCategory.isActive = true
}

function openCreateCategoryModal() {
  categoryModalMode.value = 'create'
  editingCategoryId.value = ''
  resetCategoryFormForCreate()
  categoryModalOpen.value = true
}

function openEditCategoryModal(c) {
  if (!c) return
  categoryModalMode.value = 'edit'
  editingCategoryId.value = c.id
  newCategory.name = c.name || ''
  newCategory.sort = typeof c.sort === 'number' ? c.sort : 1
  newCategory.isActive = !!c.isActive
  categoryModalOpen.value = true
}

function categoryPrevPage() {
  categoryPage.value = Math.max(1, categoryPage.value - 1)
}

function categoryNextPage() {
  categoryPage.value = Math.min(categoryTotalPages.value, categoryPage.value + 1)
}

async function submitCategoryModal() {
  const payload = {
    name: newCategory.name,
    sort: newCategory.sort,
    isActive: newCategory.isActive
  }

  if (categoryModalMode.value === 'create') {
    await postJson('/api/admin/categories', payload, { headers: headers.value })
  } else {
    await putJson(
      `/api/admin/categories/${encodeURIComponent(editingCategoryId.value)}`,
      payload,
      { headers: headers.value }
    )
  }

  categoryModalOpen.value = false
  await refreshAll()
}

const newProduct = reactive({
  categoryId: '',
  title: '',
  summary: '',
  materialsCraft: '',
  specs: '',
  imageIds: [],
  _imageIdsText: 'img_prod_1,img_prod_2,img_prod_3',
  isActive: true,
  sort: 1
})

// -----------------------------
// Products: filters + pagination + modal
// -----------------------------
const productFilters = reactive({
  categoryId: '',
  title: ''
})

const productPageSize = ref(10)
const productPage = ref(1)

const filteredProducts = computed(() => {
  const title = (productFilters.title || '').trim().toLowerCase()
  return products.value.filter(p => {
    const okCategory = !productFilters.categoryId || p.categoryId === productFilters.categoryId
    const okTitle = !title || (p.title || '').toLowerCase().includes(title)
    return okCategory && okTitle
  })
})

const productTotalPages = computed(() => {
  const total = filteredProducts.value.length
  const per = productPageSize.value || 10
  return Math.max(1, Math.ceil(total / per))
})

const productRows = computed(() => {
  const per = productPageSize.value || 10
  const start = (productPage.value - 1) * per
  return filteredProducts.value.slice(start, start + per)
})

watch(
  () => [productFilters.categoryId, productFilters.title, productPageSize.value],
  () => {
    productPage.value = 1
  }
)

watch(productPage, (v) => {
  if (v < 1) productPage.value = 1
  if (v > productTotalPages.value) productPage.value = productTotalPages.value
})

const productModalOpen = ref(false)
const productModalMode = ref('create') // 'create' | 'edit'
const editingProductId = ref('')

function productResetFormForCreate() {
  const defaultCategory =
    (productFilters.categoryId && categories.value.find(c => c.id === productFilters.categoryId))
      ? productFilters.categoryId
      : (categories.value[0]?.id || '')

  newProduct.categoryId = defaultCategory
  newProduct.title = ''
  newProduct.summary = ''
  newProduct.materialsCraft = ''
  newProduct.specs = ''
  newProduct.isActive = true
  newProduct.sort = 1
  newProduct._imageIdsText = 'img_prod_1,img_prod_2,img_prod_3'
}

function openCreateProductModal() {
  productModalMode.value = 'create'
  editingProductId.value = ''
  productResetFormForCreate()
  productModalOpen.value = true
}

function openEditProductModal(p) {
  if (!p) return
  productModalMode.value = 'edit'
  editingProductId.value = p.id

  newProduct.categoryId = p.categoryId
  newProduct.title = p.title
  newProduct.summary = p.summary
  newProduct.materialsCraft = p.materialsCraft
  newProduct.specs = p.specs
  newProduct.isActive = p.isActive
  newProduct.sort = p.sort
  newProduct._imageIdsText = p._imageIdsText || (Array.isArray(p.imageIds) ? p.imageIds.join(',') : '')
  productModalOpen.value = true
}

function productPrevPage() {
  productPage.value = Math.max(1, productPage.value - 1)
}

function productNextPage() {
  productPage.value = Math.min(productTotalPages.value, productPage.value + 1)
}

async function submitProductModal() {
  const payload = {
    categoryId: newProduct.categoryId,
    title: newProduct.title,
    summary: newProduct.summary,
    materialsCraft: newProduct.materialsCraft,
    specs: newProduct.specs,
    imageIds: normalizeImageIds(newProduct._imageIdsText),
    isActive: newProduct.isActive,
    sort: newProduct.sort
  }

  if (productModalMode.value === 'create') {
    await postJson('/api/admin/products', payload, { headers: headers.value })
  } else {
    await putJson(
      `/api/admin/products/${encodeURIComponent(editingProductId.value)}`,
      payload,
      { headers: headers.value }
    )
  }

  productModalOpen.value = false
  await refreshAll()
}

async function deleteProduct(p) {
  if (!p?.id) return
  const ok = confirm(`确定删除该产品？\n${p.title || p.id}`)
  if (!ok) return

  const res = await fetch(`/api/admin/products/${encodeURIComponent(p.id)}`, {
    method: 'DELETE',
    // headers.value 是形如 { 'X-Admin-Token': '...' } 的对象
    headers: headers.value
  })

  if (!res.ok) {
    const text = await res.text()
    alert(text || '删除失败')
    return
  }

  await refreshAll()
}

async function deleteBanner(b) {
  if (!b?.id) return
  const ok = confirm(`确定删除该轮播图？\n${b.title || b.id}`)
  if (!ok) return

  const res = await fetch(`/api/admin/banners/${encodeURIComponent(b.id)}`, {
    method: 'DELETE',
    headers: headers.value
  })

  if (!res.ok) {
    const text = await res.text()
    alert(text || '删除失败')
    return
  }

  await refreshAll()
}

async function deleteCategory(c) {
  if (!c?.id) return
  const ok = confirm(`确定删除该分类？\n${c.name || c.id}`)
  if (!ok) return

  const res = await fetch(`/api/admin/categories/${encodeURIComponent(c.id)}`, {
    method: 'DELETE',
    headers: headers.value
  })

  if (!res.ok) {
    const text = await res.text()
    alert(text || '删除失败')
    return
  }

  await refreshAll()
}

function logout() {
  localStorage.removeItem('admin_token')
  token.value = ''
  router.push('/admin/login')
}

function ensureToken() {
  if (!token.value) {
    router.push('/admin/login')
    return false
  }
  return true
}

async function saveBanner(b) {
  await putJson(`/api/admin/banners/${encodeURIComponent(b.id)}`, b, { headers: headers.value })
  await refreshAll()
}

async function createBanner() {
  await postJson('/api/admin/banners', newBanner, { headers: headers.value })
  newBanner.title = ''
  await refreshAll()
}

async function saveCategory(c) {
  await putJson(`/api/admin/categories/${encodeURIComponent(c.id)}`, c, { headers: headers.value })
  await refreshAll()
}

async function createCategory() {
  await postJson('/api/admin/categories', newCategory, { headers: headers.value })
  newCategory.name = ''
  await refreshAll()
}

async function saveProduct(p) {
  const patch = {
    ...p,
    imageIds: normalizeImageIds(p._imageIdsText)
  }
  delete patch._imageIdsText
  await putJson(`/api/admin/products/${encodeURIComponent(p.id)}`, patch, { headers: headers.value })
  await refreshAll()
}

async function createProduct() {
  const payload = {
    ...newProduct,
    imageIds: normalizeImageIds(newProduct._imageIdsText)
  }
  delete payload._imageIdsText
  await postJson('/api/admin/products', payload, { headers: headers.value })

  newProduct.title = ''
  newProduct.summary = ''
  await refreshAll()
}

async function saveCompany() {
  // 仅保存核心字段，避免把其它字段当 null 覆盖掉
  const patch = {
    companyName: company.value.companyName,
    phone: company.value.phone,
    address: company.value.address,
    weChat: company.value.weChat
  }
  await putJson('/api/admin/company', patch, { headers: headers.value })
  await refreshAll()
}

onMounted(async () => {
  if (!ensureToken()) return
  loading.value = true
  try {
    await refreshAll()
  } finally {
    loading.value = false
  }
})
</script>

