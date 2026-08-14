package com.pkshop.api.admin.claim;

import com.pkshop.common.response.ApiResponse;
import com.pkshop.domain.user.entity.User;
import com.pkshop.dto.admin.supplierclaim.CreateSupplierClaimRequest;
import com.pkshop.dto.admin.supplierclaim.SupplierClaimResponse;
import com.pkshop.service.claims.SupplierClaimService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/supplier-claims")
public class AdminSupplierClaimController {

    private final SupplierClaimService supplierClaimService;

    public AdminSupplierClaimController(SupplierClaimService supplierClaimService) {
        this.supplierClaimService = supplierClaimService;
    }

    private User currentUser() {
        return (User)  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @PostMapping
    public ApiResponse<SupplierClaimResponse> create(@Valid @RequestBody CreateSupplierClaimRequest req) {
        return  ApiResponse.ok("สร้างใบเคลมสำเร็จ", supplierClaimService.createClaim(req, currentUser()));
    }

    @GetMapping
    public ApiResponse<Page<SupplierClaimResponse>> list(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size
    ) {
        return ApiResponse.ok("Supplier claims", supplierClaimService.listClaims(status, page, size));
    }

    @GetMapping("/{id}")
    public ApiResponse<SupplierClaimResponse> detail(@PathVariable Long id) {
        return ApiResponse.ok("Supplier claim detail", supplierClaimService.getDetail(id));
    }

    @PutMapping("/{id}/cancel")
    public ApiResponse<SupplierClaimResponse> cancel(@PathVariable Long id) {
        return ApiResponse.ok("ยกเลิกเคลมสำเร็จ", supplierClaimService.cancelClaim(id, currentUser()));
    }

    @PostMapping("/{id}/receive-replacement")
    public ApiResponse<SupplierClaimResponse> receiveReplacement(@PathVariable Long id) {
        return ApiResponse.ok("รับสินค้าเปลี่ยนเข้าสต็อกสำเร็จ", supplierClaimService.receiveReplacement(id, currentUser()));
    }
}
