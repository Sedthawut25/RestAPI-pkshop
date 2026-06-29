package com.pkshop.dto.admin.promotions;

import java.util.List;

public record PromotionTargetRequest(
        List<Long> productIds,
        List<Long> categoryIds
) {}