package com.pkshop.domain.inventory.entity;

import com.pkshop.domain.catalog.entity.Product;
import com.pkshop.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name="inventory_transactions")
@Data
public class InventoryTransaction {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false)
    @JoinColumn(name="product_id")
    private Product product;

    @Column(name="txn_type", nullable=false)
    private String txnType;

    @Column(name="ref_table")
    private String refTable;

    @Column(name="ref_id")
    private Long refId;

    @Column(name="qty_change", nullable=false)
    private Integer qtyChange;

    @Column(name="unit_cost")
    private BigDecimal unitCost;

    @Column(name="note")
    private String note;

    @ManyToOne
    @JoinColumn(name="created_by")
    private User createdBy;

    @Column(name="created_at", nullable=false)
    private Instant createdAt;
}
