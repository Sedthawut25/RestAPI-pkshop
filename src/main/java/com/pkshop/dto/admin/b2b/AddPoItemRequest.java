package com.pkshop.dto.admin.b2b;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record AddPoItemRequest(
        @NotNull Long productId,
        @NotNull Integer qty,
        BigDecimal targetUnitCost
) {}
