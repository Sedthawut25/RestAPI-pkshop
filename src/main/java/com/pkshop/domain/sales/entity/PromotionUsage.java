package com.pkshop.domain.sales.entity;

import com.pkshop.domain.promotion.entity.Promotion;
import com.pkshop.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name="promotion_usages")
@Data
public class PromotionUsage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false)
    @JoinColumn(name="promotion_id")
    private Promotion promotion;

    @ManyToOne(optional=false)
    @JoinColumn(name="customer_user_id")
    private User customer;

    @ManyToOne(optional=false)
    @JoinColumn(name="order_id")
    private Order order;

    @Column(name="used_at", nullable=false)
    private LocalDateTime usedAt;
}