package com.pkshop.dto.admin.orders;

public record SupplierOption(
        Long id,
        String label,
        String email,
        String fullName
) {}