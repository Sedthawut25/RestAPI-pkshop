package com.pkshop.dto.admin.review;

import java.time.LocalDateTime;

public record AdminReviewResponse(

        Long id,
        String fullName,
        String email,
        String productName,
        Integer rating,
        String comment,
        LocalDateTime createdAt
) {

}