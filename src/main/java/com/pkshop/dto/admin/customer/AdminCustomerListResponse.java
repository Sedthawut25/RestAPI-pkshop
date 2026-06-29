package com.pkshop.dto.admin.customer;

import java.time.Instant;
import java.time.LocalDateTime;

public class AdminCustomerListResponse {

    private Long userId;
    private String fullName;
    private String email;
    private String phone;
    private Integer points;
    private String status;
    private Instant lastLoginAt;
    private LocalDateTime createdAt;

    public AdminCustomerListResponse(
            Long userId,
            String fullName,
            String email,
            String phone,
            Integer points,
            String status,
            Instant lastLoginAt,
            LocalDateTime createdAt
    ) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.points = points;
        this.status = status;
        this.lastLoginAt = lastLoginAt;
        this.createdAt = createdAt;
    }

    public Long getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public Integer getPoints() {
        return points;
    }

    public String getStatus() {
        return status;
    }

    public Instant getLastLoginAt() {
        return lastLoginAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}