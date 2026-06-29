package com.pkshop.api.customer.shop;

import com.pkshop.common.response.ApiResponse;
import com.pkshop.domain.catalog.repository.ProductRepository;
import com.pkshop.dto.customer.shop.ProductCardResponse;
import com.pkshop.dto.customer.shop.ProductDetailResponse;
import com.pkshop.dto.customer.shop.ShopFiltersResponse;
import com.pkshop.service.shop.ShopService;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional; // 🟢 เพิ่ม Import นี้
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/customer/shop/products")
@Transactional(readOnly = true)
public class CustomerProductController {

    private final ProductRepository productRepo;

    private final ShopService shopService;

    public CustomerProductController(ProductRepository productRepo, ShopService shopService) {
        this.productRepo = productRepo;
        this.shopService = shopService;
    }

    @GetMapping
    public ApiResponse<Page<ProductCardResponse>> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long brandId,
            @RequestParam(required = false) Long modelId,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false, defaultValue = "NEWEST") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size
    ) {
        Page<ProductCardResponse> p = shopService.search(
                keyword, categoryId, brandId, modelId, year, minPrice, maxPrice, sort, page, size
        );
        return ApiResponse.ok("Product", p);
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductDetailResponse> detail(@PathVariable Long id) {
        return ApiResponse.ok("Product", shopService.detail(id));
    }

    @GetMapping("/filters")
    public ApiResponse<ShopFiltersResponse> getFilters() {
        return ApiResponse.ok("Filters", shopService.filters());
    }
}