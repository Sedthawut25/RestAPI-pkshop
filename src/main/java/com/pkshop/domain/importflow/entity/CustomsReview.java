package com.pkshop.domain.importflow.entity;

import com.pkshop.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Table(name="customs_reviews")
@Data
public class CustomsReview {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false)
    @JoinColumn(name="import_document_id")
    private ImportDocument importDocument;

    @ManyToOne(optional=false)
    @JoinColumn(name="customs_user_id")
    private User customsUser;

    @Column(nullable=false)
    private String action; // APPROVE, REJECT

    @Column(columnDefinition="TEXT")
    private String comment;

    @Column(name="reviewed_at", nullable=false)
    private Instant reviewedAt;
}
