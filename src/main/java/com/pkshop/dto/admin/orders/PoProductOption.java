package com.pkshop.dto.admin.orders;

import java.math.BigDecimal;

public record PoProductOption(
        Long id,
        String sku,
        String name,
        BigDecimal importCostAvg,
        Integer stockQty
) {}