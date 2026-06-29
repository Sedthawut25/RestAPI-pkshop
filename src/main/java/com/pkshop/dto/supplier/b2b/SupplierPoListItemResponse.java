package com.pkshop.dto.supplier.b2b;

import java.math.BigDecimal;
import java.time.Instant;

public record SupplierPoListItemResponse(
        Long id,
        String poNumber,
        String status,
        String currency,
        Instant createdAt,

        Long adminId,
        String adminEmail,
        String adminFullName,

        BigDecimal itemsSubtotal
) {}