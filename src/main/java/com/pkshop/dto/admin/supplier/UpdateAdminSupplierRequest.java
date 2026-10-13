package com.pkshop.dto.admin.supplier;

import jakarta.validation.constraints.NotBlank;

public record UpdateAdminSupplierRequest(
        @NotBlank String companyName,
        @NotBlank String contactName,
        String contactEmail,
        String contactPhone
) {}
