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

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;

@Service
public class AuthService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final CustomerProfileRepository customerProfileRepository;

    public AuthService(
            UserRepository userRepo,
            RoleRepository roleRepo,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            CustomerProfileRepository customerProfileRepository
    ) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.customerProfileRepository = customerProfileRepository;
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
}