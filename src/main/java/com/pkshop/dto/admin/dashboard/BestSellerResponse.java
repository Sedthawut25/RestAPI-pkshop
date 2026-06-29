package com.pkshop.dto.admin.dashboard;

import java.math.BigDecimal;

public record BestSellerResponse(
        Long productId,
        String productName,
        long qtySold,
        BigDecimal revenue
) {}