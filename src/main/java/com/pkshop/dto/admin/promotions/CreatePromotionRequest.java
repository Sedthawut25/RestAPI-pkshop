package com.pkshop.dto.admin.promotions;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreatePromotionRequest(
        @NotBlank String code,
        @NotBlank String name,
        String description,
        @NotBlank String promoType,       // PERCENT / FIXED
        @NotNull BigDecimal value,
        BigDecimal maxDiscount,
        BigDecimal minOrderAmount,
        @NotNull LocalDateTime startAt,
        @NotNull LocalDateTime endAt,
        Boolean active,
        Integer usageLimit,
        Integer perUserLimit,
        @NotBlank String appliesTo        // ORDER / PRODUCT / CATEGORY
) {}