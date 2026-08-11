package com.pkshop.dto.user;

public record UpdateProfileRequest(
        String fullName,
        String phone
) {
}
