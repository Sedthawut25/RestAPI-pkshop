package com.pkshop.dto.admin.claim;

import jakarta.validation.constraints.NotBlank;

public record UpdateClaimStatusRequest(
        @NotBlank(message = "Status is required")
        String status,
        String adminRemark
) {
}
