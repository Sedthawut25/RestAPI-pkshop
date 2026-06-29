package com.pkshop.domain.b2b.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.pkshop.domain.catalog.entity.Product;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(
        name = "supplier_quotation_items",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_quote_product",
                columnNames = {"supplier_quotation_id", "product_id"}
        )
)
@Data
public class SupplierQuotationItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_quotation_id")
    @JsonBackReference
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private SupplierQuotation supplierQuotation;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "quoted_unit_cost", nullable = false)
    private BigDecimal quotedUnitCost;

    @Column(nullable = false)
    private Integer qty;

    @Column(name = "lead_time_days")
    private Integer leadTimeDays;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = Instant.now();
    }
}