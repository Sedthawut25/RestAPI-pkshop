package com.pkshop.dto.admin.catalog;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpsertCarModelRequest(
        @NotNull Long brandId,
        @NotBlank String name
) {}