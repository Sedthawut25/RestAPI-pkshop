package com.pkshop.api.customer.claims;

import com.pkshop.common.response.ApiResponse;
import com.pkshop.domain.user.entity.User;
import com.pkshop.dto.customer.claims.CreateClaimRequest;
import com.pkshop.service.claims.CustomerClaimService;
import com.stripe.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer/claims")
public class CustomerClaimController {

    private final CustomerClaimService customerClaimService;

    public CustomerClaimController(CustomerClaimService customerClaimService) {
        this.customerClaimService = customerClaimService;
    }

    @PostMapping
    public ApiResponse<?> submitClaim(@Valid @RequestBody CreateClaimRequest request) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        customerClaimService.submitClaim(request,user);

        return ApiResponse.ok("ส่งคำขอเคลมสำเร็จ", null);
    }

    @GetMapping
    public ApiResponse<?> getMyClaims() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var claims = customerClaimService.getMyClaims(user.getId());
        return ApiResponse.ok("ดึงประวัติการเคลมสำเร็จ", claims);
    }
}
