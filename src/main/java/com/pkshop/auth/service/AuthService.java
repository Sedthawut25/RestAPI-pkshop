package com.pkshop.auth.service;

import com.pkshop.auth.dto.AuthResponse;
import com.pkshop.auth.dto.LoginRequest;
import com.pkshop.auth.dto.RegisterRequest;
import com.pkshop.common.exception.BadRequestException;
import com.pkshop.common.exception.UnauthorizedException;
import com.pkshop.config.JwtService;

import com.pkshop.domain.user.entity.CustomerProfile;
import com.pkshop.domain.user.entity.Role;
import com.pkshop.domain.user.entity.User;

import com.pkshop.domain.user.repository.CustomerProfileRepository;
import com.pkshop.domain.user.repository.RoleRepository;
import com.pkshop.domain.user.repository.UserRepository;
import com.pkshop.auth.dto.GoogleLoginRequest;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final CustomerProfileRepository customerProfileRepository;
    private final ClerkService clerkService;

    public AuthService(
            UserRepository userRepo,
            RoleRepository roleRepo,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            CustomerProfileRepository customerProfileRepository,
            ClerkService clerkService
    ) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.customerProfileRepository = customerProfileRepository;
        this.clerkService = clerkService;
    }

    public AuthResponse register(RegisterRequest req) {

        if (userRepo.existsByEmail(req.email())) {
            throw new BadRequestException("Email already exists");
        }

        String roleName = req.role().trim().toUpperCase();

        Role role = roleRepo.findByName(roleName)
                .orElseThrow(() ->
                        new BadRequestException("Invalid role: " + roleName)
                );

        User user = new User();

        user.setEmail(req.email().toLowerCase());
        user.setPasswordHash(passwordEncoder.encode(req.password()));
        user.setFullName(req.fullName());
        user.setPhone(req.phone());

        user.getRoles().add(role);

        User savedUser = userRepo.save(user);

        // CUSTOMER PROFILE
        if (roleName.equals("CUSTOMER")) {

            CustomerProfile profile = new CustomerProfile();

            profile.setUser(savedUser);
            profile.setPoints(0);
            profile.setCreatedAt(LocalDateTime.now());

            customerProfileRepository.save(profile);
        }

        String token = jwtService.generateAccessToken(savedUser);

        return new AuthResponse(
                token,
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getFullName(),
                savedUser.getRoles()
                        .stream()
                        .map(Role::getName)
                        .toList()
        );
    }

    public AuthResponse login(LoginRequest req) {

        User user = userRepo.findByEmail(req.email().toLowerCase())
                .orElseThrow(() ->
                        new UnauthorizedException("Invalid credentials")
                );

        if (!"ACTIVE".equalsIgnoreCase(user.getStatus())) {
            throw new UnauthorizedException("User is not active");
        }

        boolean ok = passwordEncoder.matches(
                req.password(),
                user.getPasswordHash()
        );

        if (!ok) {
            throw new UnauthorizedException("Invalid credentials");
        }

        user.setLastLoginAt(Instant.now());

        userRepo.save(user);

        String token = jwtService.generateAccessToken(user);

        return new AuthResponse(
                token,
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                user.getRoles()
                        .stream()
                        .map(Role::getName)
                        .toList()
        );
    }

    public AuthResponse googleLogin(GoogleLoginRequest req) {
        System.out.println("--- เริ่มกระบวนการ Google Login ใน Service ---");
        System.out.println("อีเมลที่ส่งมาจาก Frontend: " + req.getEmail());

        String verifiedEmail = null;
        try {
            System.out.println("กำลังตรวจสอบ Token กับ Clerk...");
            verifiedEmail = clerkService.verifyTokenAndGetEmail(req.getClerkToken());
            System.out.println("✅ ตรวจสอบ Token สำเร็จ! อีเมลที่ได้จาก Clerk คือ: " + verifiedEmail);
        } catch (Exception e) {
            System.out.println("❌ พังจุดที่ 1: ตรวจสอบ Clerk Token ไม่ผ่าน! สาเหตุ: " + e.getMessage());
            throw new UnauthorizedException("Clerk Token Invalid: " + e.getMessage());
        }

        // ป้องกันคน Hack อีเมลจาก frontend ตรงกับที่ clerk บอก
        if(verifiedEmail == null || !verifiedEmail.equalsIgnoreCase(req.getEmail())) {
            System.out.println("❌ พังจุดที่ 2: อีเมลไม่ตรงกัน! (Frontend: " + req.getEmail() + " | Clerk: " + verifiedEmail + ")");
            throw new UnauthorizedException("อีเมลไม่ตรงกับเจ้าของ token");
        }

        String email = req.getEmail().toLowerCase();

        Optional<User> optionalUser = userRepo.findByEmail(email);
        User user;
        if (optionalUser.isPresent()) {
            user = optionalUser.get();

            System.out.println("เจอ User ในระบบแล้ว สถานะคือ: " + user.getStatus());
            if(user.getStatus() == null || !"ACTIVE".equalsIgnoreCase(user.getStatus())) {
                System.out.println("❌ พังจุดที่ 3: สถานะ User ไม่ใช่ ACTIVE!");
                throw new UnauthorizedException("User is not active");
            }

            user.setLastLoginAt(Instant.now());
            userRepo.save(user);
        }
        else {
            System.out.println("ไม่เจอ User ในระบบ กำลังสร้างใหม่...");
            user = new User();
            user.setEmail(email);
            user.setFullName(req.getFullName());
            user.setAuthProvider("GOOGLE");

            user.setLastLoginAt(Instant.now());
            user.setStatus("ACTIVE");

            Role role = roleRepo.findByName("CUSTOMER")
                    .orElseThrow(() -> new BadRequestException("Role CUSTOMER not found"));
            user.getRoles().add(role);
            userRepo.save(user);

            CustomerProfile profile = new CustomerProfile();
            profile.setUser(user);
            profile.setPoints(0);
            profile.setCreatedAt(LocalDateTime.now());
            customerProfileRepository.save(profile);
            System.out.println("✅ สร้าง User ใหม่สำเร็จ!");
        }

        System.out.println("✅ ออก Token ของระบบเราและเตรียมส่งคืน Frontend");
        String token = jwtService.generateAccessToken(user);
        return new AuthResponse(
                token,
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                user.getRoles().stream().map(Role::getName).toList()
        );
    }
}