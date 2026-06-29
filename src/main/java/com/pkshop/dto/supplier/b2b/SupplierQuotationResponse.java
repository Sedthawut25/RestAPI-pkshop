package com.pkshop.dto.supplier.b2b;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public record SupplierQuotationResponse(
        Long id,
        String quotationNumber,
        String status,
        LocalDate validUntil,
        Instant createdAt,
        Long purchaseOrderId,
        List<Item> items,
        BigDecimal totalAmount
) {
    public record Item(
            Long id,
            Long productId,
            String productName,
            Integer qty,
            BigDecimal quotedUnitCost,
            Integer leadTimeDays,
            BigDecimal lineTotal
    ) {}
}