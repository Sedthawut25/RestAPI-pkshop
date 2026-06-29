package com.pkshop.dto.admin.promotions;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PromotionResponse(
        Long id,
        String code,
        String name,
        String promoType,
        BigDecimal value,
        BigDecimal maxDiscount,
        BigDecimal minOrderAmount,
        LocalDateTime startAt,
        LocalDateTime endAt,
        Boolean active,
        Integer usageLimit,
        Integer perUserLimit,
        String appliesTo,
        List<Long> productIds,
        List<Long> categoryIds
) {}