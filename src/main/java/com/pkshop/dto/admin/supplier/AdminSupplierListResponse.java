package com.pkshop.dto.admin.supplier;

import java.time.LocalDateTime;

public record AdminSupplierListResponse(
        Long supplierId,
        String companyName,
        String contactName,
        String contactEmail,
        String country,
        String contactPhone,
        LocalDateTime createdAt
) {
}