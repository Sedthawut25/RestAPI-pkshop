package com.pkshop.dto.supplier.b2b;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record CreateQuotationRequest(
        LocalDate validUntil,
        List<Item> items
) {
    public record Item(
            Long productId,
            Integer qty,
            BigDecimal quotedUnitCost,
            Integer leadTimeDays
    ) {}
}
