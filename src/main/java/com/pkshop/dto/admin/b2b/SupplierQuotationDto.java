package com.pkshop.dto.admin.b2b;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public record SupplierQuotationDto(
        Long id,
        String status,
        String quotationNumber,
        Instant createdAt,
        LocalDate validUntil,
        Long purchaseOrderId,
        Long supplierUserId,
        List<ItemDto> items
) {
    public record ItemDto(
            Long id,
            Long productId,
            String productName,
            Integer qty,
            BigDecimal quotedUnitCost,
            Integer leadTimeDays
    ) {}
}