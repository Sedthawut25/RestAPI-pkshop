package com.pkshop.api.admin;

import com.pkshop.common.response.ApiResponse;
import com.pkshop.dto.admin.supplier.AdminSupplierListResponse;
import com.pkshop.dto.admin.supplier.CreateAdminSupplierRequest;
import com.pkshop.dto.admin.supplier.CreateSupplierProductRequest;
import com.pkshop.dto.admin.supplier.SupplierProductResponse;
import com.pkshop.service.admin.AdminSupplierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping
    public ApiResponse<AdminSupplierListResponse> createSupplier(
            @Valid @RequestBody CreateAdminSupplierRequest request
    ) {
        return ApiResponse.ok("สร้างซัพพลายเออร์สำเร็จ", adminSupplierService.createSupplier(request));
    }

    @PutMapping("/{supplierUserId}")
    public ApiResponse<AdminSupplierListResponse> updateSupplier(
            @PathVariable Long supplierUserId,
            @Valid @RequestBody com.pkshop.dto.admin.supplier.UpdateAdminSupplierRequest request
    ) {
        return ApiResponse.ok("แก้ไขข้อมูลซัพพลายเออร์สำเร็จ",
                adminSupplierService.updateSupplier(supplierUserId, request));
    }

    @DeleteMapping("/{supplierUserId}")
    public ApiResponse<Void> deleteSupplier(@PathVariable Long supplierUserId) {
        adminSupplierService.deleteSupplier(supplierUserId);
        return ApiResponse.ok("ลบซัพพลายเออร์เรียบร้อยแล้ว", null);
    }

    @GetMapping("/{supplierUserId}/products")
    public ApiResponse<List<SupplierProductResponse>> listSupplierProducts(
            @PathVariable Long supplierUserId,
            @RequestParam(required = false) String keyword
    ) {
        return ApiResponse.ok("รายการสินค้าที่ซัพพลายเออร์ขาย",
                adminSupplierService.listSupplierProducts(supplierUserId, keyword));
    }

    @PostMapping("/{supplierUserId}/products")
    public ApiResponse<SupplierProductResponse> addSupplierProduct(
            @PathVariable Long supplierUserId,
            @Valid @RequestBody CreateSupplierProductRequest request
    ) {
        return ApiResponse.ok("เพิ่มสินค้าให้ซัพพลายเออร์สำเร็จ",
                adminSupplierService.addSupplierProduct(supplierUserId, request));
    }

    @DeleteMapping("/{supplierUserId}/products/{productId}")
    public ApiResponse<Void> removeSupplierProduct(
            @PathVariable Long supplierUserId,
            @PathVariable Long productId
    ) {
        adminSupplierService.removeSupplierProduct(supplierUserId, productId);
        return ApiResponse.ok("นำสินค้าออกจากรายการซัพพลายเออร์แล้ว", null);
    }
}