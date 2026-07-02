package com.pkshop.api.admin.catalog;

import com.pkshop.common.response.ApiResponse;
import com.pkshop.domain.catalog.entity.Category;
import com.pkshop.domain.catalog.entity.Product;
import com.pkshop.domain.catalog.repository.CategoryRepository;
import com.pkshop.domain.catalog.repository.ProductRepository;
import com.pkshop.dto.admin.catalog.ProductResponse;
import com.pkshop.dto.admin.catalog.UpsertProductRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/products")
public class AdminProductController {

    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;

    public AdminProductController(ProductRepository productRepo, CategoryRepository categoryRepo) {
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
    }

    @GetMapping
    public ApiResponse<Page<ProductResponse>> list(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        Page<Product> p = (keyword == null || keyword.isBlank())
                ? productRepo.findAll(pageable)
                : productRepo.findByNameContainingIgnoreCaseOrSkuContainingIgnoreCase(
                keyword.trim(), keyword.trim(), pageable);

        return ApiResponse.ok("Products", p.map(this::toDto));
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductResponse> get(@PathVariable Long id) {
        Product p = productRepo.findById(id).orElseThrow();
        return ApiResponse.ok("Product", toDto(p));
    }

    @PostMapping
    public ApiResponse<ProductResponse> create(@Valid @RequestBody UpsertProductRequest req) {
        Product p = new Product();
        apply(p, req);
        p.setStockQty(0);
        Product saved = productRepo.save(p);
        return ApiResponse.ok("Created", toDto(saved));
    }

    @PutMapping("/{id}")
    public ApiResponse<ProductResponse> update(@PathVariable Long id, @Valid @RequestBody UpsertProductRequest req) {
        Product p = productRepo.findById(id).orElseThrow();
        apply(p, req);
        Product saved = productRepo.save(p);
        return ApiResponse.ok("Updated", toDto(saved));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        productRepo.deleteById(id);
        return ApiResponse.ok("Deleted", null);
    }

    private void apply(Product p, UpsertProductRequest req) {
        p.setSku(req.sku().trim());
        p.setName(req.name().trim());
        p.setPrice(req.price());
        p.setImportCostAvg(req.importCostAvg());
        p.setIsActive(req.isActive() == null ? Boolean.TRUE : req.isActive());
        p.setDescription(req.description());

        if (req.imageUrl() == null || req.imageUrl().isBlank()) {
            p.setImageUrl(null);
        } else {
            p.setImageUrl(req.imageUrl().trim());
        }

        if (req.categoryId() != null) {
            Category c = categoryRepo.findById(req.categoryId()).orElseThrow();
            p.setCategory(c);
        } else {
            p.setCategory(null);
        }
    }

    private ProductResponse toDto(Product p) {
        Long categoryId = (p.getCategory() == null) ? null : p.getCategory().getId();
        String categoryName = (p.getCategory() == null) ? null : p.getCategory().getName();
        ProductResponse.CategoryRef category = (p.getCategory() == null)
                ? null
                : new ProductResponse.CategoryRef(categoryId, categoryName);

        Boolean active = p.getIsActive();

        return new ProductResponse(
                p.getId(),
                p.getSku(),
                p.getName(),
                p.getDescription(),
                categoryId,
                categoryName,
                category,
                p.getPrice(),
                p.getImportCostAvg(),
                p.getStockQty(),
                p.getImageUrl(),
                active
        );
    }

    @GetMapping("/options")
    public ApiResponse<java.util.List<com.pkshop.dto.admin.catalog.ProductOptionResponse>> options(
            @RequestParam(defaultValue = "") String q
    ) {
        var list = productRepo.findOptionsForPo(q == null ? "" : q.trim());
        return ApiResponse.ok("Products", list);
    }

    public record ProductOption(
            Long id,
            String sku,
            String name,
            java.math.BigDecimal importCostAvg,
            Integer stockQty
    ) {}
}