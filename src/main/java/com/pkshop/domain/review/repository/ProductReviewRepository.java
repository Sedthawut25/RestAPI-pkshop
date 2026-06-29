package com.pkshop.domain.review.repository;


import com.pkshop.domain.review.entity.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {

    List<ProductReview> findByProductId(Long productId);

    List<ProductReview> findByOrderId(Long orderId);

    List<ProductReview> findAllByOrderByIdDesc();

    Optional<ProductReview> findByOrderIdAndProductIdAndUserId(Long productId, Long orderId, Long userId);
}
