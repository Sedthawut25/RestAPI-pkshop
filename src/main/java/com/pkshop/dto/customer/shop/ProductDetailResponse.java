package com.pkshop.dto.customer.shop;

import java.math.BigDecimal;
import java.util.List;

public record ProductDetailResponse(
        Long id,
        String sku,
        String name,
        BigDecimal price,
        Integer stockQty,
        String categoryName,
        String imageUrl,
        List<String> fitments // "BMW 3 Series (2016-2020)"
) {}