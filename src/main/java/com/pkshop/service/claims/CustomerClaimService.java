package com.pkshop.service.claims;

import com.pkshop.domain.sales.entity.CustomerClaim;
import com.pkshop.domain.sales.entity.CustomerClaimAttachment;
import com.pkshop.domain.sales.repository.CustomerClaimAttachmentRepository;
import com.pkshop.domain.sales.repository.CustomerClaimRepository;
import com.pkshop.domain.user.entity.User;
import com.pkshop.dto.customer.claims.CreateClaimRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerClaimService {

    private CustomerClaimRepository customerClaimRepository;
    private CustomerClaimAttachmentRepository customerClaimAttachmentRepository;

    public CustomerClaimService(CustomerClaimRepository customerClaimRepository, CustomerClaimAttachmentRepository customerClaimAttachmentRepository) {
        this.customerClaimRepository = customerClaimRepository;
        this.customerClaimAttachmentRepository = customerClaimAttachmentRepository;
    }

    @Transactional
    public CustomerClaim submitClaim (CreateClaimRequest req, User customer) {
        CustomerClaim claim = CustomerClaim.builder()
                .orderId(req.getOrderId())
                .productId(req.getProductId())
                .quantity(req.getQuantity())
                .customer(customer)
                .claimType(req.getClaimType())
                .description(req.getDescription())
                .status("PENDING")
                .build();

        claim = customerClaimRepository.save(claim);

        if (req.getFileUrls() != null && !req.getFileUrls().isEmpty()) {
            for (String url : req.getFileUrls()) {
                CustomerClaimAttachment attachment = CustomerClaimAttachment.builder()
                        .customerClaim(claim)
                        .fileUrl(url)
                        .build();
                customerClaimAttachmentRepository.save(attachment);
            }
        }
        return claim;
    }
}
