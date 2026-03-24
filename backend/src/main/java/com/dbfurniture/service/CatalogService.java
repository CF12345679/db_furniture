package com.dbfurniture.service;

import com.dbfurniture.api.dto.*;
import com.dbfurniture.entity.*;
import com.dbfurniture.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CatalogService {

    private final BannerRepository bannerRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CaseReviewRepository caseReviewRepository;
    private final CompanyProfileRepository companyProfileRepository;

    public CatalogService(BannerRepository bannerRepository,
                          CategoryRepository categoryRepository,
                          ProductRepository productRepository,
                          CaseReviewRepository caseReviewRepository,
                          CompanyProfileRepository companyProfileRepository) {
        this.bannerRepository = bannerRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.caseReviewRepository = caseReviewRepository;
        this.companyProfileRepository = companyProfileRepository;
    }

    @PostConstruct
    @Transactional
    public void initData() {
        if (categoryRepository.count() == 0) {
            String cat1Id = UUID.randomUUID().toString();
            String cat2Id = UUID.randomUUID().toString();
            String cat3Id = UUID.randomUUID().toString();

            Category c1 = createCategoryEntity(cat1Id, "红木餐桌椅", 1);
            Category c2 = createCategoryEntity(cat2Id, "红木柜类", 2);
            Category c3 = createCategoryEntity(cat3Id, "红木床/床头", 3);

            categoryRepository.saveAll(Arrays.asList(c1, c2, c3));

            productRepository.save(createProductEntity(UUID.randomUUID().toString(), c1, "红木餐桌（仿古款）", "可坐 6-8 人，稳固耐用。", 1));
            productRepository.save(createProductEntity(UUID.randomUUID().toString(), c1, "红木餐椅（四把装）", "舒适靠背，纹理清晰。", 2));
            productRepository.save(createProductEntity(UUID.randomUUID().toString(), c2, "红木四门柜", "收纳容量大，做工精细。", 1));
            productRepository.save(createProductEntity(UUID.randomUUID().toString(), c3, "红木床（1.8m）", "结实厚重，适配卧室风格。", 1));

            bannerRepository.save(createBannerEntity(UUID.randomUUID().toString(), "门店热卖：红木餐桌", "inquiry", "", 1));
            bannerRepository.save(createBannerEntity(UUID.randomUUID().toString(), "工艺讲解：榫卯结构", "custom", "/about", 2));

            caseReviewRepository.save(createCaseEntity(UUID.randomUUID().toString(), "客户案例：新中式餐厅", "选材到上漆全流程可追溯。", Arrays.asList("img_case_1"), 1));

            CompanyProfile cp = new CompanyProfile();
            cp.setId(UUID.randomUUID().toString());
            cp.setCompanyName("dbfurniture 红木家具·XX店");
            cp.setPhone("13800000000");
            cp.setAddress("XX省XX市XX区XX街道");
            cp.setWeChat("dbfurniture_wechat");
            cp.setWeChatQrImageId("img_wechat_qr_1");
            cp.setSeoTitle("农村红木家具 - 品类齐全、工艺精湛、诚信服务");
            cp.setSeoDescription("展示红木家具产品、案例与门店信息，一键添加微信咨询/联系。");
            companyProfileRepository.save(cp);
        }
    }

    public List<BannerDTO> getBanners() {
        return bannerRepository.findAllByOrderBySortAsc().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<CategoryDTO> getCategories() {
        return categoryRepository.findAllByOrderBySortAsc().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> getProducts(String categoryId) {
        List<Product> list;
        if (categoryId == null || categoryId.trim().isEmpty()) {
            list = productRepository.findAllByOrderBySortAsc();
        } else {
            list = productRepository.findByCategoryIdOrderBySortAsc(categoryId);
        }
        return list.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public ProductDTO getProductById(String productId) {
        return productRepository.findById(productId).map(this::toDTO).orElse(null);
    }

    public List<CaseReviewDTO> getCases() {
        return caseReviewRepository.findAllByOrderBySortAsc().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public CompanyProfileDTO getCompany() {
        return companyProfileRepository.findAll().stream()
                .findFirst()
                .map(this::toDTO)
                .orElse(null);
    }

    @Transactional
    public BannerDTO createBanner(BannerDTO dto) {
        Banner entity = new Banner();
        entity.setId(dto.id == null || dto.id.trim().isEmpty() ? UUID.randomUUID().toString() : dto.id);
        updateEntity(entity, dto);
        return toDTO(bannerRepository.save(entity));
    }

    @Transactional
    public boolean updateBanner(String id, BannerDTO patch) {
        return bannerRepository.findById(id).map(entity -> {
            updateEntity(entity, patch);
            bannerRepository.save(entity);
            return true;
        }).orElse(false);
    }

    @Transactional
    public CategoryDTO createCategory(CategoryDTO dto) {
        Category entity = new Category();
        entity.setId(dto.id == null || dto.id.trim().isEmpty() ? UUID.randomUUID().toString() : dto.id);
        updateEntity(entity, dto);
        return toDTO(categoryRepository.save(entity));
    }

    @Transactional
    public boolean updateCategory(String id, CategoryDTO patch) {
        return categoryRepository.findById(id).map(entity -> {
            updateEntity(entity, patch);
            categoryRepository.save(entity);
            return true;
        }).orElse(false);
    }

    @Transactional
    public ProductDTO createProduct(ProductDTO dto) {
        Product entity = new Product();
        entity.setId(dto.id == null || dto.id.trim().isEmpty() ? UUID.randomUUID().toString() : dto.id);
        updateEntity(entity, dto);
        return toDTO(productRepository.save(entity));
    }

    @Transactional
    public boolean updateProduct(String id, ProductDTO patch) {
        return productRepository.findById(id).map(entity -> {
            updateEntity(entity, patch);
            productRepository.save(entity);
            return true;
        }).orElse(false);
    }

    @Transactional
    public CompanyProfileDTO updateCompany(CompanyProfileDTO patch) {
        CompanyProfile entity = companyProfileRepository.findAll().stream().findFirst().orElseGet(() -> {
            CompanyProfile cp = new CompanyProfile();
            cp.setId(UUID.randomUUID().toString());
            return cp;
        });
        updateEntity(entity, patch);
        return toDTO(companyProfileRepository.save(entity));
    }

    @Transactional
    public boolean deleteBanner(String id) {
        if (bannerRepository.existsById(id)) {
            bannerRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean deleteCategory(String id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean deleteProduct(String id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Helper conversion methods
    private BannerDTO toDTO(Banner entity) {
        BannerDTO dto = new BannerDTO();
        dto.id = entity.getId();
        dto.title = entity.getTitle();
        dto.linkType = entity.getLinkType();
        dto.linkValue = entity.getLinkValue();
        dto.imageId = entity.getImageId();
        dto.sort = entity.getSort();
        dto.isActive = entity.getIsActive();
        return dto;
    }

    private CategoryDTO toDTO(Category entity) {
        CategoryDTO dto = new CategoryDTO();
        dto.id = entity.getId();
        dto.name = entity.getName();
        dto.sort = entity.getSort();
        dto.isActive = entity.getIsActive();
        return dto;
    }

    private ProductDTO toDTO(Product entity) {
        ProductDTO dto = new ProductDTO();
        dto.id = entity.getId();
        dto.categoryId = entity.getCategory() != null ? entity.getCategory().getId() : null;
        dto.title = entity.getTitle();
        dto.summary = entity.getSummary();
        dto.materialsCraft = entity.getMaterialsCraft();
        dto.specs = entity.getSpecs();
        dto.imageIds = new ArrayList<>(entity.getImageIds());
        dto.isActive = entity.getIsActive();
        dto.sort = entity.getSort();
        return dto;
    }

    private CaseReviewDTO toDTO(CaseReview entity) {
        CaseReviewDTO dto = new CaseReviewDTO();
        dto.id = entity.getId();
        dto.title = entity.getTitle();
        dto.content = entity.getContent();
        dto.imageIds = new ArrayList<>(entity.getImageIds());
        dto.isActive = entity.getIsActive();
        dto.sort = entity.getSort();
        return dto;
    }

    private CompanyProfileDTO toDTO(CompanyProfile entity) {
        CompanyProfileDTO dto = new CompanyProfileDTO();
        dto.id = entity.getId();
        dto.companyName = entity.getCompanyName();
        dto.phone = entity.getPhone();
        dto.address = entity.getAddress();
        dto.weChat = entity.getWeChat();
        dto.weChatQrImageId = entity.getWeChatQrImageId();
        dto.navLat = entity.getNavLat();
        dto.navLng = entity.getNavLng();
        dto.seoTitle = entity.getSeoTitle();
        dto.seoDescription = entity.getSeoDescription();
        return dto;
    }

    private void updateEntity(Banner entity, BannerDTO dto) {
        if (dto.title != null) entity.setTitle(dto.title);
        if (dto.linkType != null) entity.setLinkType(dto.linkType);
        if (dto.linkValue != null) entity.setLinkValue(dto.linkValue);
        if (dto.imageId != null) entity.setImageId(dto.imageId);
        if (dto.sort != null) entity.setSort(dto.sort);
        if (dto.isActive != null) entity.setIsActive(dto.isActive);
    }

    private void updateEntity(Category entity, CategoryDTO dto) {
        if (dto.name != null) entity.setName(dto.name);
        if (dto.sort != null) entity.setSort(dto.sort);
        if (dto.isActive != null) entity.setIsActive(dto.isActive);
    }

    private void updateEntity(Product entity, ProductDTO dto) {
        if (dto.categoryId != null) {
            entity.setCategory(categoryRepository.findById(dto.categoryId).orElse(null));
        }
        if (dto.title != null) entity.setTitle(dto.title);
        if (dto.summary != null) entity.setSummary(dto.summary);
        if (dto.materialsCraft != null) entity.setMaterialsCraft(dto.materialsCraft);
        if (dto.specs != null) entity.setSpecs(dto.specs);
        if (dto.imageIds != null) entity.setImageIds(new ArrayList<>(dto.imageIds));
        if (dto.isActive != null) entity.setIsActive(dto.isActive);
        if (dto.sort != null) entity.setSort(dto.sort);
    }

    private void updateEntity(CompanyProfile entity, CompanyProfileDTO dto) {
        if (dto.companyName != null) entity.setCompanyName(dto.companyName);
        if (dto.phone != null) entity.setPhone(dto.phone);
        if (dto.address != null) entity.setAddress(dto.address);
        if (dto.weChat != null) entity.setWeChat(dto.weChat);
        if (dto.weChatQrImageId != null) entity.setWeChatQrImageId(dto.weChatQrImageId);
        if (dto.navLat != null) entity.setNavLat(dto.navLat);
        if (dto.navLng != null) entity.setNavLng(dto.navLng);
        if (dto.seoTitle != null) entity.setSeoTitle(dto.seoTitle);
        if (dto.seoDescription != null) entity.setSeoDescription(dto.seoDescription);
    }

    private Category createCategoryEntity(String id, String name, int sort) {
        Category c = new Category();
        c.setId(id);
        c.setName(name);
        c.setSort(sort);
        c.setIsActive(true);
        return c;
    }

    private Product createProductEntity(String id, Category category, String title, String summary, int sort) {
        Product p = new Product();
        p.setId(id);
        p.setCategory(category);
        p.setTitle(title);
        p.setSummary(summary);
        p.setMaterialsCraft("占位：说明工艺/材质特点。后续可编辑富文本。");
        p.setSpecs("占位：尺寸、用料等信息。");
        p.setImageIds(Arrays.asList("img_prod_1", "img_prod_2", "img_prod_3"));
        p.setIsActive(true);
        p.setSort(sort);
        return p;
    }

    private Banner createBannerEntity(String id, String title, String linkType, String linkValue, int sort) {
        Banner b = new Banner();
        b.setId(id);
        b.setTitle(title);
        b.setLinkType(linkType);
        b.setLinkValue(linkValue);
        b.setImageId("img_banner_" + sort);
        b.setSort(sort);
        b.setIsActive(true);
        return b;
    }

    private CaseReview createCaseEntity(String id, String title, String content, List<String> imageIds, int sort) {
        CaseReview c = new CaseReview();
        c.setId(id);
        c.setTitle(title);
        c.setContent(content);
        c.setImageIds(imageIds);
        c.setIsActive(true);
        c.setSort(sort);
        return c;
    }
}
