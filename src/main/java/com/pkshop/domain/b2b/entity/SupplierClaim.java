package com.pkshop.domain.b2b.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pkshop.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "supplier_claims")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierClaim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "purchase_order_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handle"})
    private PurchaseOrder purchaseOrder;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "supplier_user_id", nullable = false)
    @JsonIgnoreProperties({"passwordHash", "roles", "orders", "hibernateLazyInitializer", "handler"})
    private User supplierUser;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "admin_user_id", nullable = false)
    @JsonIgnoreProperties({"passwordHash", "roles", "orders", "hibernateLazyInitializer", "handler"})
    private User adminUser;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "product_name", length = 200)
    private String productName;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "claim_type", nullable = false, length = 30)
    private String claimType;

    @Column(nullable = false, length = 30)
    private String status;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "supplier_response", columnDefinition = "TEXT")
    private String supplierResponse;

    @Column(name = "refund_amount")
    private BigDecimal refundAmount;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "update_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) this.status = "PENDING";
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
