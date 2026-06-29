package com.pkshop.dto.admin.importflow;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateImportLotRequest(
        @NotNull Long purchaseOrderId,
        Long supplierQuotationId,

        String originCountry,
        @NotBlank String shippingMethod,      // SEA/AIR/LAND

        @NotNull BigDecimal freightCost,
        @NotNull BigDecimal insuranceCost,
        @NotNull BigDecimal customsDutyCost,
        @NotNull BigDecimal otherCost
) {}
