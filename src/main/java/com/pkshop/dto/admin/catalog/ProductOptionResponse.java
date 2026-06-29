package com.pkshop.dto.admin.catalog;

import java.math.BigDecimal;

public record ProductOptionResponse(
        Long id,
        String sku,
        String name,
        BigDecimal importCostAvg,
        Integer stockQty
) {}