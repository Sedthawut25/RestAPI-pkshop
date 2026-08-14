package com.pkshop.dto.admin.supplierclaim;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record CreateSupplierClaimRequest(
        @NotNull(message = "กรุณาระบุ หมายเลขคำสั่งซื้อ") Long purchaseOrderId,
        @NotNull(message = "กรุณาระบุสินค้า") Long productId,
        @NotNull @Min(value = 1, message = "จำนวนต้องการมากกว่า 0") Integer quantity,
        @NotBlank(message = "กรุณาระบุประเภทเคลม") String claimType,
        @NotBlank(message = "กรุณาระบุรายละเอียด") String description,
        BigDecimal refundAmount,
        List<String> attachmentUrls
) {
}
