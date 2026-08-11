package com.pkshop.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GoogleLoginRequest {

    @NotBlank(message = "Eamil is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Full name is required")
    private String fullName;

    // Token จาก Clerk เอาไว้ยืนยันตัวตน
    @NotBlank(message = "Token is required")
    private String clerkToken;
}
