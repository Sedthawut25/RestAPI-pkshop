package com.pkshop.dto.admin.catalog;

public record FitmentResponse(
        Long id,
        Long brandId,
        String brandName,
        Long modelId,
        String modelName,
        Integer yearFrom,
        Integer yearTo
) {}