package com.pkshop.domain.b2b.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pkshop.domain.catalog.entity.Product;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(
        name="purchase_order_items",
        uniqueConstraints = @UniqueConstraint(name="uq_po_product", columnNames={"purchase_order_id","product_id"})
)
@Data
public class PurchaseOrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ ตัดวงแน่นอน
    @ManyToOne(optional=false, fetch = FetchType.LAZY)
    @JoinColumn(name="purchase_order_id")
    @JsonIgnore
    private PurchaseOrder purchaseOrder;

    // ✅ สำคัญ: Product มักมี fitments/relations อื่น → ทำ LAZY + ignore handler
    @ManyToOne(optional=false, fetch = FetchType.LAZY)
    @JoinColumn(name="product_id")
    @JsonIgnoreProperties({"fitments", "hibernateLazyInitializer", "handler"})
    private Product product;

    @Column(nullable=false)
    private Integer qty;

    @Column(name="target_unit_cost")
    private BigDecimal targetUnitCost;

    @Column(name="created_at", nullable=false)
    private Instant createdAt;
}