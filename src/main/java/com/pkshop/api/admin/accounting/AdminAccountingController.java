package com.pkshop.api.admin.accounting;

import com.pkshop.common.response.ApiResponse;
import com.pkshop.dto.admin.accounting.StripeAccountingResponse;
import com.pkshop.service.accounting.AccountingService;
import com.stripe.exception.StripeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/admin/accounting")
public class AdminAccountingController {

    private final AccountingService accountingService;

    public AdminAccountingController(AccountingService accountingService) {
        this.accountingService = accountingService;
    }

    @GetMapping("/stripe-summary")
    public ApiResponse<StripeAccountingResponse> getSummary(){
        try {
            StripeAccountingResponse data = accountingService.getStripeSummary();
            return ApiResponse.ok("Stripe accounting data fecthed successfully", data);
        }
        catch (StripeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch data from Stripe: " + e.getMessage());
        }
    }
}
