package com.pkshop.auth.controller;

import com.pkshop.auth.dto.AuthResponse;
import com.pkshop.auth.dto.LoginRequest;
import com.pkshop.auth.dto.RegisterRequest;
import com.pkshop.auth.service.AuthService;
import com.pkshop.common.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ApiResponse<AuthResponse> register(@Valid @RequestBody RegisterRequest req) {
        return ApiResponse.ok("Registered", authService.register(req));
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
        return ApiResponse.ok("Logged in", authService.login(req));
    }
}
