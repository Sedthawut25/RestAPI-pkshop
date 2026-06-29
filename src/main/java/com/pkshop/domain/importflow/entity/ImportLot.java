package com.pkshop.domain.importflow.entity;

import com.pkshop.domain.b2b.entity.PurchaseOrder;
import com.pkshop.domain.b2b.entity.SupplierQuotation;
import com.pkshop.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name="import_lots")
@Data
public class ImportLot {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="lot_number", nullable=false, unique=true)
    private String lotNumber;

    @ManyToOne(optional=false)
    @JoinColumn(name="purchase_order_id")
    private PurchaseOrder purchaseOrder;

    @ManyToOne
    @JoinColumn(name="supplier_quotation_id")
    private SupplierQuotation supplierQuotation;

    @ManyToOne(optional=false)
    @JoinColumn(name="admin_user_id")
    private User adminUser;

    @Column(name="origin_country")
    private String originCountry;

    @Column(name="shipping_method", nullable=false)
    private String shippingMethod; // SEA, AIR, LAND

    @Column(name="freight_cost", nullable=false)
    private BigDecimal freightCost;

    @Column(name="insurance_cost", nullable=false)
    private BigDecimal insuranceCost;

    @Column(name="customs_duty_cost", nullable=false)
    private BigDecimal customsDutyCost;

    @Column(name="other_cost", nullable=false)
    private BigDecimal otherCost;

    @Column(name="total_import_cost", nullable=false)
    private BigDecimal totalImportCost;

    @Column(nullable=false)
    private String status;
    // DRAFT, DOC_SENT, CUSTOMS_PENDING, CUSTOMS_APPROVED, CUSTOMS_REJECTED, RECEIVED, CLOSED

    @Column(name="arrival_date")
    private LocalDate arrivalDate;

    @Column(name="created_at", nullable=false)
    private Instant createdAt;
}
