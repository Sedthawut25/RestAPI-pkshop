/*package com.pkshop.api.admin.b2b;

import com.pkshop.common.response.ApiResponse;
import com.pkshop.domain.b2b.AdminSupplierService;
import com.pkshop.dto.admin.b2b.SupplierOptionResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/suppliers")
public class AdminSupplierController {

    private final AdminSupplierService supplierService;

    public AdminSupplierController(AdminSupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping
    public ApiResponse<List<SupplierOptionResponse>> list() {
        return ApiResponse.ok("Suppliers", supplierService.listSuppliers());
    }
}*/