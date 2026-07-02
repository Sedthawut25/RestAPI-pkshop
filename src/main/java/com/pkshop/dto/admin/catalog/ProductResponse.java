package com.pkshop.dto.admin.catalog;

import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String sku,
        String name,
        String description,

        Long categoryId,
        String categoryName,
        CategoryRef category,

        BigDecimal price,
        BigDecimal importCostAvg,
        Integer stockQty,

        String imageUrl,

        Boolean isActive
) {
    public record CategoryRef(Long id, String name) {}
}