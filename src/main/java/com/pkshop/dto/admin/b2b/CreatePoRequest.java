package com.pkshop.dto.admin.b2b;

import jakarta.validation.constraints.NotNull;

public record CreatePoRequest(
        @NotNull Long supplierUserId,
        String currency,      // default USD
        String notes
) {}
