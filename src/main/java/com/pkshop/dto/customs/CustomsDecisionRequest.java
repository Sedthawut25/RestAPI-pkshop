package com.pkshop.dto.customs;

import jakarta.validation.constraints.NotBlank;

public record CustomsDecisionRequest(
        @NotBlank String comment
) {}
