package com.pkshop.dto.admin.b2b;

import jakarta.validation.constraints.NotBlank;

public record DecideQuotationRequest(
        @NotBlank String action
) {}