package com.dbfurniture.service;

import com.dbfurniture.api.dto.BannerDTO;
import com.dbfurniture.api.dto.CaseReviewDTO;
import com.dbfurniture.api.dto.CategoryDTO;
import com.dbfurniture.api.dto.CompanyProfileDTO;
import com.dbfurniture.api.dto.ProductDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InMemoryCatalogService {

    private List<BannerDTO> banners;
    private List<CategoryDTO> categories;
    private final List<ProductDTO> products;
    private List<CaseReviewDTO> cases;
    private CompanyProfileDTO company;
    private final ConcurrentHashMap<String, ProductDTO> productById;

    public InMemoryCatalogService() {
        // 占位数据：后续管理端上线后再用上传内容替换。
        String cat1 = UUID.randomUUID().toString();
        String cat2 = UUID.randomUUID().toString();
        String cat3 = UUID.randomUUID().toString();

        CategoryDTO c1 = newCategory(cat1, "红木餐桌椅", 1);
        CategoryDTO c2 = newCategory(cat2, "红木柜类", 2);
        CategoryDTO c3 = newCategory(cat3, "红木床/床头", 3);
        categories = new ArrayList<>(Arrays.asList(c1, c2, c3));

        products = new ArrayList<>();
        products.add(newProduct(UUID.randomUUID().toString(), cat1, "红木餐桌（仿古款）", "可坐 6-8 人，稳固耐用。", 1));
        products.add(newProduct(UUID.randomUUID().toString(), cat1, "红木餐椅（四把装）", "舒适靠背，纹理清晰。", 2));
        products.add(newProduct(UUID.randomUUID().toString(), cat2, "红木四门柜", "收纳容量大，做工精细。", 1));
        products.add(newProduct(UUID.randomUUID().toString(), cat3, "红木床（1.8m）", "结实厚重，适配卧室风格。", 1));

        // 建立查询索引
        productById = new ConcurrentHashMap<>();
        for (ProductDTO p : products) {
            productById.put(p.id, p);
        }

        banners = new ArrayList<>(Arrays.asList(
                newBanner(UUID.randomUUID().toString(), "门店热卖：红木餐桌", "inquiry", "", 1),
                newBanner(UUID.randomUUID().toString(), "工艺讲解：榫卯结构", "custom", "/about", 2)
        ));

        cases = new ArrayList<>(Arrays.asList(
                newCase(UUID.randomUUID().toString(), "客户案例：新中式餐厅", "选材到上漆全流程可追溯。", Collections.singletonList("img_case_1"), 1)
        ));

        company = new CompanyProfileDTO();
        company.id = UUID.randomUUID().toString();
        company.companyName = "dbfurniture 红木家具·XX店";
        company.phone = "13800000000";
        company.address = "XX省XX市XX区XX街道";
        company.weChat = "dbfurniture_wechat";
        company.weChatQrImageId = "img_wechat_qr_1";
        company.navLat = null;
        company.navLng = null;
        company.seoTitle = "农村红木家具 - 品类齐全、工艺精湛、诚信服务";
        company.seoDescription = "展示红木家具产品、案例与门店信息，一键添加微信咨询/联系。";
    }

    public List<BannerDTO> getBanners() {
        return banners;
    }

    public List<CategoryDTO> getCategories() {
        return categories;
    }

    public List<ProductDTO> getProducts(String categoryId) {
        if (categoryId == null || categoryId.trim().isEmpty()) {
            return products;
        }
        List<ProductDTO> res = new ArrayList<>();
        for (ProductDTO p : products) {
            if (categoryId.equals(p.categoryId)) {
                res.add(p);
            }
        }
        return res;
    }

    public ProductDTO getProductById(String productId) {
        return productById.get(productId);
    }

    public List<CaseReviewDTO> getCases() {
        return cases;
    }

    public CompanyProfileDTO getCompany() {
        return company;
    }

    // -----------------------------
    // Admin mutators (MVP, in-memory)
    // -----------------------------

    public synchronized BannerDTO createBanner(BannerDTO dto) {
        if (dto == null) dto = new BannerDTO();
        dto.id = dto.id == null || dto.id.trim().isEmpty() ? UUID.randomUUID().toString() : dto.id;
        dto.title = dto.title == null ? "" : dto.title;
        dto.linkType = dto.linkType == null ? "custom" : dto.linkType;
        dto.linkValue = dto.linkValue == null ? "" : dto.linkValue;
        dto.imageId = dto.imageId == null ? "" : dto.imageId;
        dto.sort = dto.sort == null ? 0 : dto.sort;
        dto.isActive = dto.isActive == null ? true : dto.isActive;
        banners.add(dto);
        return dto;
    }

    public synchronized boolean updateBanner(String id, BannerDTO patch) {
        if (id == null || id.trim().isEmpty() || patch == null) return false;
        for (BannerDTO b : banners) {
            if (id.equals(b.id)) {
                if (patch.title != null) b.title = patch.title;
                if (patch.linkType != null) b.linkType = patch.linkType;
                if (patch.linkValue != null) b.linkValue = patch.linkValue;
                if (patch.imageId != null) b.imageId = patch.imageId;
                if (patch.sort != null) b.sort = patch.sort;
                if (patch.isActive != null) b.isActive = patch.isActive;
                return true;
            }
        }
        return false;
    }

    public synchronized CategoryDTO createCategory(CategoryDTO dto) {
        if (dto == null) dto = new CategoryDTO();
        dto.id = dto.id == null || dto.id.trim().isEmpty() ? UUID.randomUUID().toString() : dto.id;
        dto.name = dto.name == null ? "" : dto.name;
        dto.sort = dto.sort == null ? 0 : dto.sort;
        dto.isActive = dto.isActive == null ? true : dto.isActive;
        categories.add(dto);
        return dto;
    }

    public synchronized boolean updateCategory(String id, CategoryDTO patch) {
        if (id == null || id.trim().isEmpty() || patch == null) return false;
        for (CategoryDTO c : categories) {
            if (id.equals(c.id)) {
                if (patch.name != null) c.name = patch.name;
                if (patch.sort != null) c.sort = patch.sort;
                if (patch.isActive != null) c.isActive = patch.isActive;
                return true;
            }
        }
        return false;
    }

    public synchronized ProductDTO createProduct(ProductDTO dto) {
        if (dto == null) dto = new ProductDTO();
        dto.id = dto.id == null || dto.id.trim().isEmpty() ? UUID.randomUUID().toString() : dto.id;
        dto.categoryId = dto.categoryId == null ? "" : dto.categoryId;
        dto.title = dto.title == null ? "" : dto.title;
        dto.summary = dto.summary == null ? "" : dto.summary;
        dto.materialsCraft = dto.materialsCraft == null ? "" : dto.materialsCraft;
        dto.specs = dto.specs == null ? "" : dto.specs;
        dto.imageIds = dto.imageIds == null ? new ArrayList<>() : dto.imageIds;
        dto.isActive = dto.isActive == null ? true : dto.isActive;
        dto.sort = dto.sort == null ? 0 : dto.sort;

        products.add(dto);
        productById.put(dto.id, dto);
        return dto;
    }

    public synchronized boolean updateProduct(String id, ProductDTO patch) {
        if (id == null || id.trim().isEmpty() || patch == null) return false;
        ProductDTO existing = productById.get(id);
        if (existing == null) return false;

        if (patch.categoryId != null) existing.categoryId = patch.categoryId;
        if (patch.title != null) existing.title = patch.title;
        if (patch.summary != null) existing.summary = patch.summary;
        if (patch.materialsCraft != null) existing.materialsCraft = patch.materialsCraft;
        if (patch.specs != null) existing.specs = patch.specs;
        if (patch.imageIds != null) existing.imageIds = patch.imageIds;
        if (patch.isActive != null) existing.isActive = patch.isActive;
        if (patch.sort != null) existing.sort = patch.sort;
        return true;
    }

    public synchronized CompanyProfileDTO updateCompany(CompanyProfileDTO patch) {
        if (patch == null) return company;
        if (patch.companyName != null) company.companyName = patch.companyName;
        if (patch.phone != null) company.phone = patch.phone;
        if (patch.address != null) company.address = patch.address;
        if (patch.weChat != null) company.weChat = patch.weChat;
        if (patch.weChatQrImageId != null) company.weChatQrImageId = patch.weChatQrImageId;
        if (patch.seoTitle != null) company.seoTitle = patch.seoTitle;
        if (patch.seoDescription != null) company.seoDescription = patch.seoDescription;
        if (patch.navLat != null) company.navLat = patch.navLat;
        if (patch.navLng != null) company.navLng = patch.navLng;
        return company;
    }

    public synchronized boolean deleteBanner(String id) {
        if (id == null || id.trim().isEmpty()) return false;
        return banners.removeIf(b -> id.equals(b.id));
    }

    public synchronized boolean deleteCategory(String id) {
        if (id == null || id.trim().isEmpty()) return false;
        boolean ok = categories.removeIf(c -> id.equals(c.id));
        if (!ok) return false;

        // MVP：不做级联删除，保留产品但访客端会显示“分类-”。
        // 也可以在后续版本选择级联删除或自动归类。
        return true;
    }

    public synchronized boolean deleteProduct(String id) {
        if (id == null || id.trim().isEmpty()) return false;
        ProductDTO existing = productById.remove(id);
        if (existing == null) return false;
        return products.removeIf(p -> id.equals(p.id));
    }

    private static CategoryDTO newCategory(String id, String name, int sort) {
        CategoryDTO dto = new CategoryDTO();
        dto.id = id;
        dto.name = name;
        dto.sort = sort;
        dto.isActive = true;
        return dto;
    }

    private static ProductDTO newProduct(String id, String categoryId, String title, String summary, int sort) {
        ProductDTO dto = new ProductDTO();
        dto.id = id;
        dto.categoryId = categoryId;
        dto.title = title;
        dto.summary = summary;
        dto.materialsCraft = "占位：说明工艺/材质特点。后续可编辑富文本。";
        dto.specs = "占位：尺寸、用料等信息。";
        dto.imageIds = Arrays.asList("img_prod_1", "img_prod_2", "img_prod_3");
        dto.isActive = true;
        dto.sort = sort;
        return dto;
    }

    private static BannerDTO newBanner(String id, String title, String linkType, String linkValue, int sort) {
        BannerDTO dto = new BannerDTO();
        dto.id = id;
        dto.title = title;
        dto.linkType = linkType;
        dto.linkValue = linkValue;
        dto.imageId = "img_banner_" + sort;
        dto.sort = sort;
        dto.isActive = true;
        return dto;
    }

    private static CaseReviewDTO newCase(String id, String title, String content, List<String> imageIds, int sort) {
        CaseReviewDTO dto = new CaseReviewDTO();
        dto.id = id;
        dto.title = title;
        dto.content = content;
        dto.imageIds = imageIds;
        dto.isActive = true;
        dto.sort = sort;
        return dto;
    }
}

