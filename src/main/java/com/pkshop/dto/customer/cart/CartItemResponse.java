package com.pkshop.dto.customer.cart;

import java.math.BigDecimal;

public record CartItemResponse(
        Long id,
        Long productId,
        String sku,
        String productName,
        BigDecimal unitPrice,
        Integer qty,
        BigDecimal lineTotal,
        Integer stockQty,
        String imageUrl
) {}