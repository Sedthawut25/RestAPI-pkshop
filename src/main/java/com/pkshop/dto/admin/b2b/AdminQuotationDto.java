package com.pkshop.dto.admin.b2b;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record AdminQuotationDto(
        Long id,
        String quotationNumber,
        String status,
        LocalDate validUntil,
        Long supplierUserId,
        String supplierEmail,
        Long purchaseOrderId,
        String poNumber,
        List<Item> items
) {
    public record Item(Long productId, Integer qty, BigDecimal quotedUnitCost, Integer leadTimeDays) {}
}