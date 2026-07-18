package com.pkshop.service.claims;

import com.pkshop.domain.sales.entity.CustomerClaim;
import com.pkshop.domain.sales.entity.CustomerClaimAttachment;
import com.pkshop.domain.sales.entity.Order;
import com.pkshop.domain.sales.repository.CustomerClaimAttachmentRepository;
import com.pkshop.domain.sales.repository.CustomerClaimRepository;
import com.pkshop.domain.sales.repository.OrderRepository;
import com.pkshop.domain.user.entity.User;
import com.pkshop.dto.customer.claims.CreateClaimRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerClaimService {

    private final CustomerClaimRepository customerClaimRepository;
    private final CustomerClaimAttachmentRepository customerClaimAttachmentRepository;
    private final OrderRepository orderRepository;

    public CustomerClaimService(CustomerClaimRepository customerClaimRepository,
                                CustomerClaimAttachmentRepository customerClaimAttachmentRepository,
                                OrderRepository orderRepository) {
        this.customerClaimRepository = customerClaimRepository;
        this.customerClaimAttachmentRepository = customerClaimAttachmentRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public CustomerClaim submitClaim(CreateClaimRequest req, User customer) {
        CustomerClaim claim = CustomerClaim.builder()
                .orderId(req.getOrderId())
                .productId(req.getProductId())
                .productName(req.getProductName())
                .quantity(req.getQuantity())
                .customer(customer)
                .claimType(req.getClaimType())
                .description(req.getDescription())
                .status("PENDING")
                .imageUrl(req.getImageUrl())
                .build();

        claim = customerClaimRepository.save(claim);

        if (req.getImageUrl() != null && !req.getImageUrl().isBlank()) {
            CustomerClaimAttachment attachment = CustomerClaimAttachment.builder()
                    .customerClaim(claim)
                    .fileUrl(req.getImageUrl()) // ใช้ getImageUrl() ตรงตาม DTO
                    .build();
            customerClaimAttachmentRepository.save(attachment);
        }

        return claim;
    }

    public List<CustomerClaim> getMyClaims(Long customerId) {
        List<CustomerClaim> claims = customerClaimRepository.findByCustomerIdOrderByCreatedAtDesc(customerId);

        for (CustomerClaim claim : claims) {
            if ("EXCHANGE".equals(claim.getClaimType()) && claim.getReplacementOrderId() != null) {
                Optional<Order> repOrder = orderRepository.findById(claim.getReplacementOrderId());
                if (repOrder.isPresent()) {
                    claim.setReplacementTrackingNumber(repOrder.get().getTrackingNumber());
                    claim.setReplacementShippingProvider(repOrder.get().getShippingProvider());
                }
            }
        }

        return claims;
    }
}