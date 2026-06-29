package com.pkshop.service.review;

import com.pkshop.common.exception.BadRequestException;
import com.pkshop.domain.catalog.entity.Product;
import com.pkshop.domain.catalog.repository.ProductRepository;
import com.pkshop.domain.review.entity.ProductReview;
import com.pkshop.domain.review.repository.ProductReviewRepository;
import com.pkshop.domain.sales.entity.Order;
import com.pkshop.domain.sales.repository.OrderRepository;
import com.pkshop.domain.user.entity.User;
import com.pkshop.dto.admin.review.AdminReviewResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewService {

    private final ProductReviewRepository productReviewRepository;

    private final ProductRepository productRepository;

    private final OrderRepository orderRepository;

    public ReviewService(

            ProductReviewRepository productReviewRepository,

            ProductRepository productRepository,

            OrderRepository orderRepository

    ) {

        this.productReviewRepository = productReviewRepository;

        this.productRepository = productRepository;

        this.orderRepository = orderRepository;
    }

    // CUSTOMER REVIEW
    public void createReview(

            User user,

            Long orderId,

            Long productId,

            Integer rating,

            String comment

    ) {

        Order order = orderRepository
                .findById(orderId)
                .orElseThrow();

        // รีวิวได้เฉพาะ delivered
        if (!"DELIVERED".equals(order.getStatus())) {

            throw new BadRequestException(
                    "รีวิวได้เฉพาะออเดอร์ที่จัดส่งสำเร็จ"
            );
        }

        // เช็ครีวิวซ้ำ
        boolean exists = productReviewRepository
                .findByOrderIdAndProductIdAndUserId(
                        orderId,
                        productId,
                        user.getId()
                )
                .isPresent();

        if (exists) {

            throw new BadRequestException(
                    "คุณรีวิวสินค้านี้แล้ว"
            );
        }

        Product product = productRepository
                .findById(productId)
                .orElseThrow();

        ProductReview review = new ProductReview();

        review.setOrder(order);

        review.setProduct(product);

        review.setUser(user);

        review.setRating(rating);

        review.setComment(comment);

        review.setCreatedAt(LocalDateTime.now());

        productReviewRepository.save(review);
    }

    public List<AdminReviewResponse> adminList() {
        return productReviewRepository
                .findAllByOrderByIdDesc()
                .stream()
                .map(r -> new AdminReviewResponse(
                        r.getId(),
                        r.getUser().getFullName(),
                        r.getUser().getEmail(),
                        r.getProduct().getName(),
                        r.getRating(),
                        r.getComment(),
                        r.getCreatedAt()

                ))
                .toList();
    }
}