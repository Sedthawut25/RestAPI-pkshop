package com.pkshop.domain.b2b.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pkshop.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "supplier_quotations")
@Data
public class SupplierQuotation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_order_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private PurchaseOrder purchaseOrder;

    @Column(name = "quotation_number", nullable = false, unique = true)
    private String quotationNumber;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_user_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private User supplierUser;

    @Column(nullable = false)
    private String status; // SUBMITTED, ACCEPTED, REJECTED

    @Column(name = "valid_until")
    private LocalDate validUntil;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @OneToMany(
            mappedBy = "supplierQuotation",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JsonManagedReference
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<SupplierQuotationItem> items = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = Instant.now();
    }
}