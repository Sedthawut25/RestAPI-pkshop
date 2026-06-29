package com.pkshop.dto.admin.promotions;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UpdatePromotionRequest(
        String name,
        String description,
        String promoType,
        BigDecimal value,
        BigDecimal maxDiscount,
        BigDecimal minOrderAmount,
        LocalDateTime startAt,
        LocalDateTime endAt,
        Boolean active,
        Integer usageLimit,
        Integer perUserLimit,
        String appliesTo
) {}