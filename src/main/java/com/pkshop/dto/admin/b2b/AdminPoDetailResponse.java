package com.pkshop.dto.admin.b2b;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record AdminPoDetailResponse(
        Long id,
        String poNumber,
        String status,
        String currency,
        Instant createdAt,
        SupplierRef supplier,
        AdminRef admin,
        List<Item> items
) {
    public record SupplierRef(Long id, String fullName, String email) {}
    public record AdminRef(Long id, String fullName, String email) {}

    public record Item(
            Long id,
            Long productId,
            String sku,
            String name,
            Integer qty,
            BigDecimal targetUnitCost
    ) {}
}