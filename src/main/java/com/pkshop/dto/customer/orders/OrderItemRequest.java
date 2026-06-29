package com.pkshop.dto.customer.orders;

public record OrderItemRequest(
        Long productId,
        Integer qty
) {}
