package com.pkshop.domain.sales.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name="payments")
@Data
public class Payment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false)
    @JoinColumn(name="order_id")
    private Order order;

    @Column(name="provider", nullable=false)
    private String provider; // STRIPE

    @Column(name="status", nullable=false)
    private String status; // REQUIRES_PAYMENT_METHOD, REQUIRES_CONFIRMATION, SUCCEEDED, FAILED

    @Column(name="amount", nullable=false)
    private BigDecimal amount;

    @Column(name="currency", nullable=false)
    private String currency; // thb/usd

    @Column(name="stripe_payment_intent_id")
    private String stripePaymentIntentId;

    @Column(name="created_at", nullable=false)
    private Instant createdAt;

    @Column(name="updated_at")
    private Instant updatedAt;
}
