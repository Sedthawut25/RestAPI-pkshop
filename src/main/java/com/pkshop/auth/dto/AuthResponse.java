package com.pkshop.auth.dto;

import java.util.List;

public record AuthResponse(
        String accessToken,
        Long userId,
        String email,
        String fullName,
        List<String> roles
) {}
