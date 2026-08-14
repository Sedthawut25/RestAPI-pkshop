package com.pkshop.dto.admin.supplierclaim;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record SupplierClaimResponse(
        Long id,
        Long purchaseOrderId,
        String poNumber,
        Long productId,
        String productName,
        Integer quantity,
        String claimType,
        String status,
        String description,
        String supplierResponse,
        BigDecimal refundAmount,
        SupplierInfo supplier,
        AdminInfo admin,
        List<String> attachments,
        LocalDateTime resolvedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt

) {
    public record SupplierInfo(Long id, String fullName, String email) {}
    public record AdminInfo(Long id, String fullName, String email) {}
}
