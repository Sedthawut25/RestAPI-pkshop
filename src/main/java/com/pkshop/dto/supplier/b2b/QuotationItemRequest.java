package com.pkshop.dto.supplier.b2b;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record QuotationItemRequest(
        @NotNull Long productId,
        @NotNull Integer qty,
        @NotNull BigDecimal quotedUnitCost,
        Integer leadTimeDays
) {}
