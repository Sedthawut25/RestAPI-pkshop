package com.pkshop.api.customer.review;

import com.pkshop.common.response.ApiResponse;
import com.pkshop.domain.user.entity.User;
import com.pkshop.domain.user.repository.UserRepository;
import com.pkshop.dto.customer.review.CreateReviewRequest;
import com.pkshop.service.review.ReviewService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            throw new RuntimeException("Unauthenticated user");
        }

        Object principal = auth.getPrincipal();

        // 🟢 Case 1: หาก Principal เป็นวัตถุ User Entity อยู่แล้ว (ใช้ได้ทันที!)
        if (principal instanceof User user) {
            return user;
        }

        // 🟢 Case 2: หาก Principal เป็น UserDetails ของ Spring Security
        if (principal instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            return userRepository.findByEmail(username)
                    .orElseGet(() -> userRepository.findById(Long.parseLong(username))
                            .orElseThrow(() -> new RuntimeException("User not found: " + username)));
        }

        // 🟢 Case 3: หาก Principal เป็น String (เช่น Email หรือ User ID)
        if (principal instanceof String identifier) {
            return userRepository.findByEmail(identifier)
                    .orElseGet(() -> {
                        try {
                            Long userId = Long.parseLong(identifier);
                            return userRepository.findById(userId)
                                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
                        } catch (NumberFormatException e) {
                            throw new RuntimeException("User not found with Email: " + identifier);
                        }
                    });
        }

        throw new RuntimeException("Unsupported principal type: " + principal.getClass().getName());
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
