package com.pkshop.dto.admin.supplier;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateAdminSupplierRequest(
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8, max = 100) String initialPassword,
        @NotBlank String fullName,
        String phone,
        @NotBlank String companyName,
        String contactName,
        String contactEmail,
        String contactPhone
) {}