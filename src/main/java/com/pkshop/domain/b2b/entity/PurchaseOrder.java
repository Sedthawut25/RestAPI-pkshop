package com.pkshop.domain.b2b.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pkshop.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name="purchase_orders")
@Data
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="po_number", nullable=false, unique=true)
    private String poNumber;

    // ✅ สำคัญ: ทำเป็น LAZY กัน Jackson ไล่ไปโหลด roles/user_roles ฯลฯ
    @ManyToOne(optional=false, fetch = FetchType.LAZY)
    @JoinColumn(name="admin_user_id")
    @JsonIgnoreProperties({"passwordHash", "roles", "userRoles", "hibernateLazyInitializer", "handler"})
    private User adminUser;

    @ManyToOne(optional=false, fetch = FetchType.LAZY)
    @JoinColumn(name="supplier_user_id")
    @JsonIgnoreProperties({"passwordHash", "roles", "userRoles", "hibernateLazyInitializer", "handler"})
    private User supplierUser;

    @Column(nullable=false)
    private String status; // DRAFT, SENT, QUOTED, CONFIRMED, ...

    @Column(nullable=false)
    private String currency; // USD/THB

    @Column(name="created_at", nullable=false)
    private Instant createdAt;
}