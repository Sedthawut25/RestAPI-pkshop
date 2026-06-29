package com.pkshop.dto.admin.orders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record AdminOrderDetailResponse(
        Long orderId,
        String orderNumber,
        String status,
        BigDecimal subtotal,
        BigDecimal shippingFee,
        BigDecimal discount,
        BigDecimal grandTotal,
        LocalDateTime createdAt,
        CustomerInfo customer,
        List<Item> items,
        ShipmentInfo shipment,
        String shippingProvider,
        String trackingNumber
) {
    public record CustomerInfo(Long id, String email) {}

    public record Item(
            Long productId,
            String productName,
            Integer qty,
            BigDecimal unitPrice,
            BigDecimal lineTotal
    ) {}

    public record ShipmentInfo(
            String carrier,
            String trackingNo,
            String status,
            LocalDateTime shippedAt,
            LocalDateTime deliveredAt
    ) {}
}