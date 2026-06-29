package com.pkshop.dto.customer.orders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderDetailResponse(
        Long orderId,
        String orderNumber,
        String status,
        BigDecimal subTotal,
        BigDecimal shippingFee,
        BigDecimal discount,
        BigDecimal grandTotal,
        LocalDateTime createdAt,
        List<Item> items,
        ShipmentInfo shipment,
        String shippingProvider,
        String trackingNumber,
        String shippingAddress
) {
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