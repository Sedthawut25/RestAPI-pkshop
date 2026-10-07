package com.pkshop.auth.service;

import com.pkshop.auth.dto.RegisterRequest;
import com.pkshop.common.exception.BadRequestException;
import com.pkshop.config.JwtService;
import com.pkshop.domain.user.repository.CustomerProfileRepository;
import com.pkshop.domain.user.repository.RoleRepository;
import com.pkshop.domain.user.repository.UserRepository;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepo;

    @Mock
    private RoleRepository roleRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private CustomerProfileRepository customerProfileRepository;

    @Mock
    private ClerkService clerkService;

    @InjectMocks
    private AuthService authService;

    @ParameterizedTest
    @ValueSource(strings = {"ADMIN", "CUSTOMS"})
    void publicRegistrationRejectsPrivilegedRoles(String role) {
        RegisterRequest request = new RegisterRequest(
                "user@example.com",
                "password123",
                "Test User",
                null,
                role
        );

        assertThrows(BadRequestException.class, () -> authService.register(request));
        verifyNoInteractions(
                userRepo,
                roleRepo,
                passwordEncoder,
                jwtService,
                customerProfileRepository,
                clerkService
        );
    }
}