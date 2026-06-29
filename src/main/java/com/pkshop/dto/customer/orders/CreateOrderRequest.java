package com.pkshop.dto.customer.orders;

import java.math.BigDecimal;
import java.util.List;

public record CreateOrderRequest(
        String promotionCode,
        String shippingAddress,
        String note,
        BigDecimal discount,
        List<OrderItemRequest> items
) {}
