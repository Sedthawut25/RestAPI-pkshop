package com.pkshop.domain.sales.repository;

import com.pkshop.domain.sales.entity.CustomerClaimAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerClaimAttachmentRepository extends JpaRepository<CustomerClaimAttachment,Long> {

    List<CustomerClaimAttachment> findByCustomerClaim_Id(Long claimId);
}
