package com.pkshop.service.shop;

import com.pkshop.domain.catalog.entity.Product;
import com.pkshop.domain.catalog.repository.*;
import com.pkshop.dto.customer.shop.ProductCardResponse;
import com.pkshop.dto.customer.shop.ProductDetailResponse;
import com.pkshop.dto.customer.shop.ShopFiltersResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ShopService {

    private final ProductQueryRepository productQueryRepo;
    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;
    private final CarBrandRepository brandRepo;
    private final CarModelRepository modelRepo;

    public ShopService(ProductQueryRepository productQueryRepo,
                       ProductRepository productRepo,
                       CategoryRepository categoryRepo,
                       CarBrandRepository brandRepo,
                       CarModelRepository modelRepo) {
        this.productQueryRepo = productQueryRepo;
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
        this.brandRepo = brandRepo;
        this.modelRepo = modelRepo;
    }

    public Page<ProductCardResponse> search(
            String keyword, Long categoryId, Long brandId, Long modelId, Integer year,
            BigDecimal minPrice, BigDecimal maxPrice, String sort, int page, int size
    ) {
        String safeSort = (sort == null || sort.isBlank()) ? "NEWEST" : sort.trim().toUpperCase();

        Page<Product> result;

        try {
            result = productQueryRepo.search(
                    keyword, categoryId, brandId, modelId, year,
                    minPrice, maxPrice,
                    safeSort, page, size
            );
        } catch (Exception ex) {
            System.err.println("ShopService search error: " + ex.getMessage());
            ex.printStackTrace();
            var pageable = org.springframework.data.domain.PageRequest.of(
                    Math.max(page, 0),
                    Math.max(size, 1),
                    org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "id")
            );
            if (keyword != null && !keyword.isBlank()) {
                result = productRepo.findByNameContainingIgnoreCaseOrSkuContainingIgnoreCase(
                        keyword.trim(), keyword.trim(), pageable
                );
            } else {
                result = productRepo.findAll(pageable);
            }
        }
        return result.map(p -> new ProductCardResponse(
                p.getId(),
                p.getSku(),
                p.getName(),
                p.getPrice(),
                p.getStockQty(),
                p.getCategory() != null ? p.getCategory().getName() : null,
                p.getImageUrl()
        ));
    }

    public ProductDetailResponse detail(Long productId) {
        Product p = productRepo.findByIdWithDetails(productId).orElseThrow();

        List<String> fitments = (p.getFitments() == null ? List.<String>of() :
                p.getFitments().stream()
                        .map(f -> {
                            var model = f.getCarModel();
                            var brand = (model != null ? model.getBrand() : null);
                            String brandName = (brand != null ? brand.getName() : "-");
                            String modelName = (model != null ? model.getName() : "-");
                            return brandName + " " + modelName + " (" + f.getYearFrom() + "-" + f.getYearTo() + ")";
                        })
                        .toList()
        );

        return new ProductDetailResponse(
                p.getId(),
                p.getSku(),
                p.getName(),
                p.getPrice(),
                p.getStockQty(),
                p.getCategory() != null ? p.getCategory().getName() : null,
                p.getImageUrl(),
                p.getDescription(),
                fitments
        );
    }

    public ShopFiltersResponse filters() {
        var cats = categoryRepo.findAll().stream()
                .map(c -> new ShopFiltersResponse.IdName(c.getId(), c.getName()))
                .toList();

        var brands = brandRepo.findAll().stream()
                .map(b -> new ShopFiltersResponse.IdName(b.getId(), b.getName()))
                .toList();

        var models = modelRepo.findAll().stream()
                .map(m -> new ShopFiltersResponse.ModelDto(m.getId(), m.getName(), m.getBrand().getId()))
                .toList();

        return new ShopFiltersResponse(cats, brands, models);
    }
}