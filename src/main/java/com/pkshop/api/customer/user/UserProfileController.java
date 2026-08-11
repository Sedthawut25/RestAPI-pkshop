package com.pkshop.api.customer.user;

import com.pkshop.domain.user.entity.User;
import com.pkshop.domain.user.repository.UserRepository;
import com.pkshop.dto.user.UpdateProfileRequest;
import com.pkshop.dto.user.UpdatePasswordRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/customer/profile")
public class UserProfileController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserProfileController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 1. ดึงโปรไฟล์ปัจจุบัน
    @GetMapping
    public ResponseEntity<?> getMyProfile(Authentication authentication) {
        // 🛠️ Cast อ็อบเจกต์ Principal กลับมาเป็นคลาส User แล้วดึงอีเมล
        User currentUser = (User) authentication.getPrincipal();
        String email = currentUser.getEmail();

        System.out.println("========== DEBUG: Extracted Email is -> " + email + " ==========");

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("ไม่พบผู้ใช้งานด้วยค่า: " + email));

        Map<String, String> response = new HashMap<>();
        response.put("fullName", user.getFullName());
        response.put("email", user.getEmail());
        response.put("phone", user.getPhone());

        return ResponseEntity.ok(response);
    }

    // 2. อัปเดตข้อมูลส่วนตัว
    @PutMapping
    public ResponseEntity<?> updateProfile(@RequestBody UpdateProfileRequest request, Authentication authentication) {
        // 🛠️ Cast และดึงอีเมล
        User currentUser = (User) authentication.getPrincipal();
        String email = currentUser.getEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("ไม่พบผู้ใช้งานด้วยค่า: " + email));

        user.setFullName(request.fullName());
        user.setPhone(request.phone());
        userRepository.save(user);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "อัปเดตข้อมูลส่วนตัวสำเร็จ");

        Map<String, String> userData = new HashMap<>();
        userData.put("fullName", user.getFullName());
        userData.put("email", user.getEmail());
        userData.put("phone", user.getPhone());
        response.put("user", userData);

        return ResponseEntity.ok(response);
    }

    // 3. เปลี่ยนรหัสผ่าน
    @PutMapping("/password")
    public ResponseEntity<?> updatePassword(@Valid @RequestBody UpdatePasswordRequest request, Authentication authentication) {
        // 🛠️ Cast และดึงอีเมล
        User currentUser = (User) authentication.getPrincipal();
        String email = currentUser.getEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("ไม่พบผู้ใช้งานด้วยค่า: " + email));

        // ตรวจรหัสผ่านเดิมตรงมั้ย
        if (!passwordEncoder.matches(request.oldPassword(), user.getPasswordHash())) {
            return ResponseEntity.badRequest().body("รหัสผ่านเดิมไม่ถูกต้อง");
        }

        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);

        return ResponseEntity.ok("เปลี่ยนรหัสผ่านสำเร็จ");
    }
}