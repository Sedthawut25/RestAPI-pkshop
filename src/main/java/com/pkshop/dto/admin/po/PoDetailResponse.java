package com.pkshop.dto.admin.po;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record PoDetailResponse(
        Long id,
        String poNumber,
        String status,
        String currency,
        Instant createdAt,
        SupplierRef supplier,
        List<Item> items
) {
    public record SupplierRef(Long id, String fullName, String email) {}
    public record Item(Long id, Long productId, String sku, String name, Integer qty, BigDecimal targetUnitCost) {}
}