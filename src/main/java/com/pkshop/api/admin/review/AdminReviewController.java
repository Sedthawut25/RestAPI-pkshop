package com.pkshop.api.admin.review;

import com.pkshop.common.response.ApiResponse;
import com.pkshop.dto.admin.review.AdminReviewResponse;
import com.pkshop.service.review.ReviewService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/reviews")
public class AdminReviewController {

    private final ReviewService reviewService;

    public AdminReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public ApiResponse<List<AdminReviewResponse>> list() {
        return ApiResponse.ok(
                "Revs",
                reviewService.adminList()
        );
    }
}
