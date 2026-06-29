package com.pkshop.dto.supplier.b2b;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record SupplierPoDetailResponse(
        Long id,
        String poNumber,
        String status,
        String currency,
        Instant createdAt,

        AdminInfo admin,
        List<Item> items,

        BigDecimal itemsSubtotal
) {
    public record AdminInfo(
            Long id,
            String email,
            String fullName
    ) {}

    public record Item(
            Long id,
            Long productId,
            String sku,
            String productName,
            Integer qty,
            BigDecimal targetUnitCost,
            BigDecimal lineTotal
    ) {}
}