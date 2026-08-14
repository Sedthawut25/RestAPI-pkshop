package com.pkshop.api.supplier.claims;

import com.pkshop.common.response.ApiResponse;
import com.pkshop.domain.user.entity.User;
import com.pkshop.dto.admin.supplierclaim.SupplierClaimResponse;
import com.pkshop.dto.supplier.claims.ResponseSupplierClaimRequest;
import com.pkshop.service.claims.SupplierClaimService;
import jakarta.validation.Valid;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/supplier/claims")
public class SupplierClaimController {

    private final SupplierClaimService supplierClaimService;

    public SupplierClaimController(SupplierClaimService supplierClaimService) {
        this.supplierClaimService = supplierClaimService;
    }

    private User currentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @GetMapping
    public ApiResponse<List<SupplierClaimResponse>> list(@RequestParam(required = false) String status) {
        return ApiResponse.ok("My claims", supplierClaimService.listForSupplier(currentUser(), status));
    }

    @GetMapping("/{id}")
    public ApiResponse<SupplierClaimResponse> detail(@PathVariable Long id) {
        return ApiResponse.ok("Claim detail", supplierClaimService.getDetailForSupplier(id, currentUser()));
    }

    @PutMapping("/{id}/respond")
    public ApiResponse<SupplierClaimResponse> respond(
            @PathVariable Long id,
            @Valid @RequestBody ResponseSupplierClaimRequest req
    ) {
        return ApiResponse.ok("ตอบกลับเคลมสำเร็จ",
                supplierClaimService.response(id, currentUser(), req.action(), req.response()));
    }
}