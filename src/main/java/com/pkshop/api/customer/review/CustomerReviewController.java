package com.pkshop.api.customer.review;

import com.pkshop.common.response.ApiResponse;
import com.pkshop.domain.user.entity.User;
import com.pkshop.domain.user.repository.UserRepository;
import com.pkshop.dto.customer.review.CreateReviewRequest;
import com.pkshop.service.review.ReviewService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer/reviews")
public class CustomerReviewController {

    private final ReviewService reviewService;
    private final UserRepository userRepository;

    public CustomerReviewController(ReviewService reviewService, UserRepository userRepository) {
        this.reviewService = reviewService;
        this.userRepository = userRepository;
    }

    private User currentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow();
    }

    @PostMapping("/{orderId}")
    public ApiResponse<?> createReview(
            @PathVariable Long orderId,
            @RequestBody CreateReviewRequest req
    ) {
        reviewService.createReview(
                currentUser(),
                orderId,
                req.productId(),
                req.rating(),
                req.comment()
        );

        return ApiResponse.ok(
                "Review created",
                null
        );
    }
}
