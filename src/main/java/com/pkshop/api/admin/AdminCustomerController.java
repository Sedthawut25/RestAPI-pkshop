package com.pkshop.api.admin;

import com.pkshop.common.response.ApiResponse;
import com.pkshop.dto.admin.customer.AdminCustomerListResponse;
import com.pkshop.service.admin.AdminCustomerService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/customers")
public class AdminCustomerController {

    private final AdminCustomerService adminCustomerService;

    public AdminCustomerController(AdminCustomerService adminCustomerService) {
        this.adminCustomerService = adminCustomerService;
    }

    @GetMapping
    public ApiResponse<Page<AdminCustomerListResponse>> listCustomers(

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size

    ) {
        return ApiResponse.ok(
                "Customer List",
                adminCustomerService.listAdminCustomers(
                        page,
                        size
                )
        );
    }
}