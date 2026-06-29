package com.pkshop.dto.customer.orders;

import java.math.BigDecimal;

public record OrderListItemResponse(
        Long orderId,
        String orderNumber,
        String status,
        BigDecimal grandTotal,
        java.time.LocalDateTime createdAt
) {}