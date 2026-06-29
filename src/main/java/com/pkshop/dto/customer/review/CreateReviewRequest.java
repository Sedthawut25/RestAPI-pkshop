package com.pkshop.dto.customer.review;

public record CreateReviewRequest (
    Long productId,
    Integer rating,
    String comment
){}
