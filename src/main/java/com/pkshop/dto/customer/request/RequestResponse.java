package com.pkshop.dto.customer.request;

import java.time.LocalDateTime;

public record RequestResponse(
        Long id,
        Long productId,
        String productName,
        Long requestedBrandId,
        Long requestModelId,
        Integer year,
        String description,
        String imageUrl,
        String status,
        String adminNote,
        LocalDateTime createdAt,
        String partName,
        String carBrand,
        String carModel
) {
}