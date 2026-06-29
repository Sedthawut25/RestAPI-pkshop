package com.pkshop.dto.customer.request;

public record CreateRequestDto (
        Long productId,
        Long requestedBrandId,
        Long requestModelId,
        Integer year,
        String description,
        String imageUrl,
        String partName,
        String carBrand,
        String carModel
) {}