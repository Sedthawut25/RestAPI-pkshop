package com.pkshop.dto.customer.cart;

import jakarta.validation.constraints.NotNull;

public record AddToCartRequest(
        @NotNull Long productId,
        @NotNull Integer qty
) {}
