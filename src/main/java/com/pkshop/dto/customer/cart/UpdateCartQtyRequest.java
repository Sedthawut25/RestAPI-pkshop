package com.pkshop.dto.customer.cart;

import jakarta.validation.constraints.NotNull;

public record UpdateCartQtyRequest(
        @NotNull Integer qty
) {}
