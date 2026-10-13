package com.pkshop.domain.supplier.entity;

import com.pkshop.domain.catalog.entity.Product;
import com.pkshop.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(
        name = "supplier_products",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_supplier_product",
                columnNames = {"supplier_user_id", "product_id"}
        )
)
@Getter
@Setter
@NoArgsConstructor
public class SupplierProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_user_id", nullable = false)
    private User supplierUser;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "supplier_sku", length = 100)
    private String supplierSku;

    @Column(name = "min_order_qty", nullable = false)
    private Integer minOrderQty = 1;

    @Column(name = "is_active", nullable = false)
    private Boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    void onCreate() {
        if (createdAt == null) createdAt = Instant.now();
        if (minOrderQty == null) minOrderQty = 1;
        if (active == null) active = true;
    }
}