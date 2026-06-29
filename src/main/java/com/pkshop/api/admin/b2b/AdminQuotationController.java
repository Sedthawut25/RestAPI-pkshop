package com.pkshop.api.admin.b2b;

import com.pkshop.common.response.ApiResponse;
import com.pkshop.dto.admin.b2b.SupplierQuotationDto;
import com.pkshop.mapper.b2b.SupplierQuotationMapper;
import com.pkshop.service.b2b.B2bService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/quotations")
public class AdminQuotationController {

    private final B2bService b2bService;

    public AdminQuotationController(B2bService b2bService) {
        this.b2bService = b2bService;
    }

    @PostMapping("/{quotationId}/accept")
    public ApiResponse<SupplierQuotationDto> accept(@PathVariable Long quotationId) {
        var q = b2bService.acceptQuotation(quotationId);
        return ApiResponse.ok("Quotation accepted", SupplierQuotationMapper.toDto(q));
    }

    @PostMapping("/{quotationId}/reject")
    public ApiResponse<SupplierQuotationDto> reject(@PathVariable Long quotationId) {
        var q = b2bService.rejectQuotation(quotationId);
        return ApiResponse.ok("Quotation rejected", SupplierQuotationMapper.toDto(q));
    }
}