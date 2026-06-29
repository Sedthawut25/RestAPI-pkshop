package com.pkshop.dto.customer.checkout;

import jakarta.validation.constraints.NotNull;

public record CreatePaymentIntentRequest(
        @NotNull Long orderId,
        String currency
) {}
