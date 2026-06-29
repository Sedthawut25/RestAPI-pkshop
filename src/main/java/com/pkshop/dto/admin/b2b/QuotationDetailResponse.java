package com.pkshop.dto.admin.b2b;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Instant;
import java.util.List;

public record QuotationDetailResponse(
        Long id,
        String quotationNumber,
        String status,
        LocalDate validUntil,
        String remarks,
        Instant createdAt,
        List<Item> items
) {
    public record Item(
            Long id,
            Long productId,
            String sku,
            String name,
            Integer qty,
            BigDecimal quotedUnitCost,
            Integer leadTimeDays
    ) {}
}
