package com.pkshop.service.admin;

import com.pkshop.domain.supplier.repository.SupplierProfileRepository;
import com.pkshop.dto.admin.supplier.AdminSupplierListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminSupplierService {

    private final SupplierProfileRepository supplierProfileRepository;

    public Page<AdminSupplierListResponse> listSuppliers(
            int page,
            int size
    ) {
        return supplierProfileRepository.findAdminSuppliers(
                PageRequest.of(page, size)
        );
    }
}