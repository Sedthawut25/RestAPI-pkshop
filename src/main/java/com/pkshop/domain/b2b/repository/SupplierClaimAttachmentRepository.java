package com.pkshop.domain.b2b.repository;

import com.pkshop.domain.b2b.entity.SupplierClaimAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupplierClaimAttachmentRepository extends JpaRepository<SupplierClaimAttachment, Long> {
    List<SupplierClaimAttachment> findBySupplierClaim_Id(Long supplierClaimId);
}