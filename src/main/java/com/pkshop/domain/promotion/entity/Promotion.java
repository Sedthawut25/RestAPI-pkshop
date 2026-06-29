package com.pkshop.domain.promotion.entity;

import com.pkshop.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="promotions")
@Data
public class Promotion {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true, length=50)
    private String code;

    @Column(nullable=false, length=150)
    private String name;

    @Column(columnDefinition="TEXT")
    private String description;

    @Column(name="promo_type", nullable=false, length=20)
    private String promoType; // PERCENT / FIXED

    @Column(nullable=false, precision=12, scale=2)
    private BigDecimal value;

    @Column(name="max_discount", precision=12, scale=2)
    private BigDecimal maxDiscount;

    @Column(name="min_order_amount", precision=12, scale=2)
    private BigDecimal minOrderAmount;

    @Column(name="start_at", nullable=false)
    private LocalDateTime startAt;

    @Column(name="end_at", nullable=false)
    private LocalDateTime endAt;

    @Column(name="is_active", nullable=false)
    private Boolean active;

    @Column(name="usage_limit")
    private Integer usageLimit;

    @Column(name="per_user_limit")
    private Integer perUserLimit;

    @Column(name="applies_to", nullable=false, length=20)
    private String appliesTo; // ORDER / PRODUCT / CATEGORY

    @ManyToOne
    @JoinColumn(name="created_by")
    private User createdBy;
}