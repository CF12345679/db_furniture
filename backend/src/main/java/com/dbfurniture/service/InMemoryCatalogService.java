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

    private final List<BannerDTO> banners;
    private final List<CategoryDTO> categories;
    private final List<ProductDTO> products;
    private final List<CaseReviewDTO> cases;
    private final CompanyProfileDTO company;
    private final ConcurrentHashMap<String, ProductDTO> productById;

    public InMemoryCatalogService() {
        // 占位数据：后续管理端上线后再用上传内容替换。
        String cat1 = UUID.randomUUID().toString();
        String cat2 = UUID.randomUUID().toString();
        String cat3 = UUID.randomUUID().toString();

        CategoryDTO c1 = newCategory(cat1, "红木餐桌椅", 1);
        CategoryDTO c2 = newCategory(cat2, "红木柜类", 2);
        CategoryDTO c3 = newCategory(cat3, "红木床/床头", 3);
        categories = Arrays.asList(c1, c2, c3);

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

        banners = Arrays.asList(
                newBanner(UUID.randomUUID().toString(), "门店热卖：红木餐桌", "inquiry", "", 1),
                newBanner(UUID.randomUUID().toString(), "工艺讲解：榫卯结构", "custom", "/about", 2)
        );

        cases = Arrays.asList(
                newCase(UUID.randomUUID().toString(), "客户案例：新中式餐厅", "选材到上漆全流程可追溯。", Collections.singletonList("img_case_1"), 1)
        );

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

