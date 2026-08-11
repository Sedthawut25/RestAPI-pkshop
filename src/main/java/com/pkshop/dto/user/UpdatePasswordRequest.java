package com.pkshop.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdatePasswordRequest(
        @NotBlank(message = "กรุณากรอกรหัสผ่านเดิม")
        String oldPassword,

        @NotBlank(message = "กรุณากรอกรหัสผ่านใหม่")
        @Size(min = 8, max = 100, message = "รหัสผ่านใหม่ต้องมีความยาวอย่างน้อย 8 ตัวอักษร")
        String newPassword
) {
}