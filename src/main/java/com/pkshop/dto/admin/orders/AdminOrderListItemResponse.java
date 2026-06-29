package com.pkshop.dto.admin.orders;

import java.math.BigDecimal;

public record AdminOrderListItemResponse(
        Long orderId,
        String orderNumber,
        String status,
        BigDecimal grandTotal,
        java.time.LocalDateTime createdAt,
        Long customerId,
        String customerEmail,
        String fullName
) {}