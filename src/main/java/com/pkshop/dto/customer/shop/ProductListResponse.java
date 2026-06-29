package com.pkshop.dto.customer.shop;

import java.math.BigDecimal;

public record ProductListResponse(
        Long id,
        String sku,
        String name,
        BigDecimal price,
        Integer stockQty,
        String imageUrl
) {}