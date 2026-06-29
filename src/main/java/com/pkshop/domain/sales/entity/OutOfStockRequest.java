package com.pkshop.domain.sales.entity;

import com.pkshop.domain.catalog.entity.Product;
import com.pkshop.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "out_of_stock_requests")
@Data
public class OutOfStockRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_user_id")
    private User customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private String partName;
    private String carBrand;
    private String carModel;

    @Column(name = "requested_brand_id")
    private Long requestedBrandId;

    @Column(name = "requested_model_id")
    private Long requestedModelId;

    @Column(name = "year")
    private Integer year;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "status", nullable = false, length = 30)
    private String status = "OPEN";

    @Column(name = "admin_note", columnDefinition = "TEXT")
    private String adminNote;

    @CreationTimestamp
    @Column(name = "created_At", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_At")
    private LocalDateTime updatedAt;
}
