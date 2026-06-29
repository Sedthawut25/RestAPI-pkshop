package com.pkshop.domain.supplier.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "supplier_profiles")
@Getter
@Setter
public class SupplierProfile {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "tax_id")
    private String taxId;

    @Column(name = "contact_name")
    private String contactName;

    @Column(name = "contact_email")
    private String contactEmail;

    @Column(name = "contact_phone")
    private String contactPhone;

    private String country;

    @Column(name = "address_text")
    private String addressText;

    @Column(name = "bank_account_text")
    private String bankAccountText;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}