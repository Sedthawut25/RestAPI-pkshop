package com.pkshop.dto.admin.catalog;

import jakarta.validation.constraints.NotNull;

public record CreateFitmentRequest(
        @NotNull Long carModelId,
        @NotNull Integer yearFrom,
        @NotNull Integer yearTo
) {}
