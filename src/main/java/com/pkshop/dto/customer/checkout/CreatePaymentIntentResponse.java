package com.pkshop.dto.customer.checkout;

public record CreatePaymentIntentResponse(
        String clientSecret,
        String paymentIntentId
) {}
