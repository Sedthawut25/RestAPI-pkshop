package com.pkshop.dto.admin.dashboard;

public record DeadStockResponse(
        Long productId,
        String productName,
        Integer stockQty,
        Integer daysSinceLastSale
) {}