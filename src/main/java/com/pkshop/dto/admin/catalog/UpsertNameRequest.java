package com.pkshop.dto.admin.catalog;

import jakarta.validation.constraints.NotBlank;

public record UpsertNameRequest(@NotBlank String name) {}