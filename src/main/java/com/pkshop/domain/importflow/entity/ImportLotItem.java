package com.pkshop.domain.importflow.entity;

import com.pkshop.domain.catalog.entity.Product;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name="import_lot_items",
        uniqueConstraints = @UniqueConstraint(name="uq_lot_product", columnNames={"import_lot_id","product_id"}))
@Data
public class ImportLotItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false)
    @JoinColumn(name="import_lot_id")
    private ImportLot importLot;

    @ManyToOne(optional=false)
    @JoinColumn(name="product_id")
    private Product product;

    @Column(nullable=false)
    private Integer qty;

    @Column(name="unit_cost", nullable=false)
    private BigDecimal unitCost;

    @Column(name="line_cost", nullable=false)
    private BigDecimal lineCost;

    @Column(name="created_at", nullable=false)
    private Instant createdAt;
}
