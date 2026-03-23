package com.dbfurniture.api.controller;

import com.dbfurniture.api.dto.BannerDTO;
import com.dbfurniture.api.dto.CaseReviewDTO;
import com.dbfurniture.api.dto.CategoryDTO;
import com.dbfurniture.api.dto.CompanyProfileDTO;
import com.dbfurniture.api.dto.ProductDTO;
import com.dbfurniture.service.InMemoryCatalogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CatalogController {

    private final InMemoryCatalogService catalogService;

    public CatalogController(InMemoryCatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping("/banners")
    public List<BannerDTO> banners() {
        return catalogService.getBanners();
    }

    @GetMapping("/categories")
    public List<CategoryDTO> categories() {
        return catalogService.getCategories();
    }

    @GetMapping("/products")
    public List<ProductDTO> products(@RequestParam(value = "categoryId", required = false) String categoryId) {
        return catalogService.getProducts(categoryId);
    }

    @GetMapping("/products/{productId}")
    public ProductDTO product(@PathVariable("productId") String productId) {
        return catalogService.getProductById(productId);
    }

    @GetMapping("/cases")
    public List<CaseReviewDTO> cases() {
        return catalogService.getCases();
    }

    @GetMapping("/company")
    public CompanyProfileDTO company() {
        return catalogService.getCompany();
    }
}

