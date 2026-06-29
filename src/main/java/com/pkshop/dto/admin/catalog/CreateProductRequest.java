package com.pkshop.dto.admin.catalog;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateProductRequest(
        @NotBlank String sku,
        @NotBlank String name,
        Long categoryId,
        @NotNull BigDecimal price,
        @NotNull BigDecimal importCostAvg,
        String imageUrl,
        Boolean isActive
) {}