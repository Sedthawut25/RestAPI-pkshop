package com.pkshop.api.admin.claim;

import com.pkshop.common.response.ApiResponse;
import com.pkshop.domain.sales.entity.CustomerClaim;
import com.pkshop.dto.admin.claim.UpdateClaimStatusRequest;
import com.pkshop.service.admin.AdminClaimService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/claims")
public class AdminClaimController {

    private final AdminClaimService adminClaimService;

    public AdminClaimController(AdminClaimService adminClaimService) {
        this.adminClaimService = adminClaimService;
    }

    @GetMapping
    public ApiResponse<Page<CustomerClaim>> listClaims(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size
    ) {
        Page<CustomerClaim> claims = adminClaimService.listClaims(keyword, status, page, size);
        return ApiResponse.ok("Success", claims);
    }

    @GetMapping("/{id}")
    public ApiResponse<CustomerClaim> getClaim(@PathVariable Long id){
        CustomerClaim detail = adminClaimService.getClaimDetail(id);
        return ApiResponse.ok("Success", detail);
    }

    @PutMapping("/{id}/status")
    public ApiResponse<Void> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateClaimStatusRequest request
    ) {
        try {
            adminClaimService.updateClaimStatus(id, request);
            return ApiResponse.ok("อัแเดตสถานะการเคลมเรียบร้อย", null);
        }
        catch (Exception ex){
            //ดักเมื่อคืนเงินไม่ผ่าน โยน Exception มาหน้าบ้านทันที
            throw new RuntimeException("เกิดข้อผิดพลาด: " + ex.getMessage());
        }

    }
}
