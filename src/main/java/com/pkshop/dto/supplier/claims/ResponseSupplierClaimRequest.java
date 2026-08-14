package com.pkshop.dto.supplier.claims;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record ResponseSupplierClaimRequest(
        @JsonProperty("action")
        @NotBlank(message = "กรุณาระบุ action (APPROVE/REJECT)")
        String action,

        @JsonProperty("response")
        String response
) {
}