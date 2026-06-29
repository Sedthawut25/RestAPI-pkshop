package com.pkshop.api.admin;

import com.pkshop.service.admin.AdminSupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/suppliers")
@RequiredArgsConstructor
public class AdminSupplierController {

    private final AdminSupplierService adminSupplierService;

    @GetMapping
    public Object listSuppliers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return adminSupplierService.listSuppliers(page, size);
    }
}