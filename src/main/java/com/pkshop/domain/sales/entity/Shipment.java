package com.pkshop.domain.sales.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name="shipments")
@Data
public class Shipment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional=false)
    @JoinColumn(name="order_id", unique = true)
    private Order order;

    private String carrier;

    @Column(name="tracking_no")
    private String trackingNo;

    @Column(nullable=false)
    private String status; // PENDING, SHIPPED, DELIVERED

    @Column(name="shipped_at")
    private LocalDateTime shippedAt;

    @Column(name="delivered_at")
    private LocalDateTime deliveredAt;
}