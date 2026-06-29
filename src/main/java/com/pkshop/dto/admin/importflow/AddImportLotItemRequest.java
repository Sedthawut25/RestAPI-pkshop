package com.pkshop.dto.admin.importflow;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record AddImportLotItemRequest(
        @NotNull Long productId,
        @NotNull Integer qty,
        @NotNull BigDecimal unitCost
) {}
