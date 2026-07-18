package com.pkshop.dto.customer.orders;

import java.math.BigDecimal;

public record OrderSummaryResponse(
        Long orderId,
        String orderNumber,
        String status,
        BigDecimal subtotal,
        BigDecimal shippingFee,
        BigDecimal discount,
        BigDecimal grandTotal,
        String checkoutUrl
) {}
