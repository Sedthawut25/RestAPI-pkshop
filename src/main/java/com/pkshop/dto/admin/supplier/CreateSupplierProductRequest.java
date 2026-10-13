package com.pkshop.dto.admin.supplier;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateSupplierProductRequest(
        @NotNull Long productId,
        @Size(max = 100) String supplierSku,
        @Min(1) Integer minOrderQty
) {}