package com.pkshop.dto.admin.catalog;

import java.math.BigDecimal;
import java.util.List;

public record ProductListItemResponse(
        Long id,
        String sku,
        String name,
        Long categoryId,
        String categoryName,
        BigDecimal price,
        BigDecimal importCostAvg,
        Integer stockQty,
        Boolean isActive,
        List<Fitment> fitments
) {
    public record Fitment(Long brandId, String brandName, Long modelId, String modelName, Integer yearFrom, Integer yearTo) {}
}