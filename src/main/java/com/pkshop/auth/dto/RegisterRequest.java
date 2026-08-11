package com.pkshop.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "กรุณากรอกอีเมล")
        @Email(message = "รูปแบบอีเมลไม่ถูกต้อง")
        String email,

        @NotBlank(message = "กรุณากรอกรหัสผ่าน")
        @Size(min = 8, max = 100, message = "รหัสผ่านต้องมีความยาวอย่างน้อย 8 ตัวอักษร")
        String password,

        @NotBlank(message = "กรุณากรอกชื่อ-นามสกุล")
        String fullName,

        String phone,

        @NotBlank(message = "กรุณาระบุบทบาท")
        String role
) {}