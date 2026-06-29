package com.pkshop.domain.importflow.entity;

import com.pkshop.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Table(name="import_documents")
@Data
public class ImportDocument {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false)
    @JoinColumn(name="import_lot_id")
    private ImportLot importLot;

    @Column(name="doc_number", nullable=false, unique=true)
    private String docNumber;

    @Column(name="doc_type", nullable=false)
    private String docType; // E_IMPORT, INVOICE, ...

    @Column(nullable=false)
    private String status; // SENT, UNDER_REVIEW, APPROVED, REJECTED

    @ManyToOne(optional=false)
    @JoinColumn(name="submitted_by_admin_id")
    private User submittedBy;

    @Column(name="submitted_at", nullable=false)
    private Instant submittedAt;

    @Column(name="comment", length = 500)
    private String comment;
}
