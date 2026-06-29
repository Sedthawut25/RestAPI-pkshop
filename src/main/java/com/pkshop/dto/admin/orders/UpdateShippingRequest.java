package com.pkshop.dto.admin.orders;

public record UpdateShippingRequest(
        String shippingProvider,
        String trackingNumber
) {
}