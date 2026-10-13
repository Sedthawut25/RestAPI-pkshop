package com.pkshop.dto.admin.supplier;

import java.math.BigDecimal;

public record SupplierProductResponse(
        Long id,
        Long productId,
        String productSku,
        String productName,
        Integer stockQty,
        BigDecimal storePrice,
        String supplierSku,
        Integer minOrderQty
) {}