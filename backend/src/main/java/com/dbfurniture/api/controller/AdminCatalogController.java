package com.dbfurniture.api.controller;

import com.dbfurniture.api.dto.BannerDTO;
import com.dbfurniture.api.dto.CategoryDTO;
import com.dbfurniture.api.dto.CompanyProfileDTO;
import com.dbfurniture.api.dto.ProductDTO;
import com.dbfurniture.service.InMemoryCatalogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminCatalogController {

    private static final String ADMIN_TOKEN = "dev-admin-token";

    private final InMemoryCatalogService catalogService;

    public AdminCatalogController(InMemoryCatalogService catalogService) {
        this.catalogService = catalogService;
    }

    private boolean authorized(String token) {
        return token != null && ADMIN_TOKEN.equals(token);
    }

    @GetMapping("/banners")
    public ResponseEntity<List<BannerDTO>> banners(@RequestHeader(value = "X-Admin-Token", required = false) String token) {
        if (!authorized(token)) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(catalogService.getBanners(), HttpStatus.OK);
    }

    @PostMapping("/banners")
    public ResponseEntity<BannerDTO> createBanner(
            @RequestHeader(value = "X-Admin-Token", required = false) String token,
            @RequestBody BannerDTO dto
    ) {
        if (!authorized(token)) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        BannerDTO created = catalogService.createBanner(dto);
        return new ResponseEntity<>(created, HttpStatus.OK);
    }

    @PutMapping("/banners/{id}")
    public ResponseEntity<Object> updateBanner(
            @RequestHeader(value = "X-Admin-Token", required = false) String token,
            @PathVariable("id") String id,
            @RequestBody BannerDTO patch
    ) {
        if (!authorized(token)) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        boolean ok = catalogService.updateBanner(id, patch);
        return ok ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDTO>> categories(
            @RequestHeader(value = "X-Admin-Token", required = false) String token
    ) {
        if (!authorized(token)) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(catalogService.getCategories(), HttpStatus.OK);
    }

    @PostMapping("/categories")
    public ResponseEntity<CategoryDTO> createCategory(
            @RequestHeader(value = "X-Admin-Token", required = false) String token,
            @RequestBody CategoryDTO dto
    ) {
        if (!authorized(token)) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        CategoryDTO created = catalogService.createCategory(dto);
        return new ResponseEntity<>(created, HttpStatus.OK);
    }

    @PutMapping("/categories/{id}")
    public ResponseEntity<Object> updateCategory(
            @RequestHeader(value = "X-Admin-Token", required = false) String token,
            @PathVariable("id") String id,
            @RequestBody CategoryDTO patch
    ) {
        if (!authorized(token)) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        boolean ok = catalogService.updateCategory(id, patch);
        return ok ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductDTO>> products(
            @RequestHeader(value = "X-Admin-Token", required = false) String token
    ) {
        if (!authorized(token)) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        // MVP：不分页、不筛选
        return new ResponseEntity<>(catalogService.getProducts(null), HttpStatus.OK);
    }

    @PostMapping("/products")
    public ResponseEntity<ProductDTO> createProduct(
            @RequestHeader(value = "X-Admin-Token", required = false) String token,
            @RequestBody ProductDTO dto
    ) {
        if (!authorized(token)) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        ProductDTO created = catalogService.createProduct(dto);
        return new ResponseEntity<>(created, HttpStatus.OK);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Object> updateProduct(
            @RequestHeader(value = "X-Admin-Token", required = false) String token,
            @PathVariable("id") String id,
            @RequestBody ProductDTO patch
    ) {
        if (!authorized(token)) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        boolean ok = catalogService.updateProduct(id, patch);
        return ok ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/company")
    public ResponseEntity<CompanyProfileDTO> company(
            @RequestHeader(value = "X-Admin-Token", required = false) String token
    ) {
        if (!authorized(token)) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(catalogService.getCompany(), HttpStatus.OK);
    }

    @PutMapping("/company")
    public ResponseEntity<Object> updateCompany(
            @RequestHeader(value = "X-Admin-Token", required = false) String token,
            @RequestBody CompanyProfileDTO patch
    ) {
        if (!authorized(token)) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        catalogService.updateCompany(patch);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Object> deleteProduct(
            @RequestHeader(value = "X-Admin-Token", required = false) String token,
            @PathVariable("id") String id
    ) {
        if (!authorized(token)) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        boolean ok = catalogService.deleteProduct(id);
        return ok ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Object> deleteCategory(
            @RequestHeader(value = "X-Admin-Token", required = false) String token,
            @PathVariable("id") String id
    ) {
        if (!authorized(token)) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        boolean ok = catalogService.deleteCategory(id);
        return ok ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/banners/{id}")
    public ResponseEntity<Object> deleteBanner(
            @RequestHeader(value = "X-Admin-Token", required = false) String token,
            @PathVariable("id") String id
    ) {
        if (!authorized(token)) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        boolean ok = catalogService.deleteBanner(id);
        return ok ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}

