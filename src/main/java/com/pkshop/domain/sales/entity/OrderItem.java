package com.pkshop.domain.sales.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pkshop.domain.catalog.entity.Product;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name="order_items")
@Data
public class OrderItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonBackReference
    private Order order;

    @ManyToOne(optional=false)
    @JoinColumn(name="product_id")
    private Product product;

    @Column(nullable=false)
    private Integer qty;

    @Column(name="unit_price", nullable=false)
    private BigDecimal unitPrice;

    @Column(name="line_total", nullable=false)
    private BigDecimal lineTotal;

    @Column(name="created_at", nullable=false)
    private Instant createdAt;

    @Column(name = "product_name_snapshot", nullable = false)
    private String productNameSnapshot;

    @Column(name = "product_image_snapshot")
    private String productImageSnapshot;
}
