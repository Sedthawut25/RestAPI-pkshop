package com.pkshop.dto.admin.orders;

import java.time.LocalDateTime;

public record UpsertShipmentRequest(
        String carrier,
        String trackingNo,
        String status,          // PENDING/SHIPPED/DELIVERED (optional)
        LocalDateTime shippedAt,
        LocalDateTime deliveredAt
) {}