package com.pkshop.domain.sales.entity;

import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;

@Entity
@Table(name = "customer_claim_attachments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CustomerClaimAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_claim_id", nullable = false)
    private   CustomerClaim customerClaim;

    @Column(name = "file_url", nullable = false, length = 500)
    private String fileUrl;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
