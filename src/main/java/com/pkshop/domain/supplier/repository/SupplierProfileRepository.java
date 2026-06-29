package com.pkshop.domain.supplier.repository;

import com.pkshop.domain.supplier.entity.SupplierProfile;
import com.pkshop.dto.admin.supplier.AdminSupplierListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SupplierProfileRepository
        extends JpaRepository<SupplierProfile, Long> {

    @Query("""
        SELECT new com.pkshop.dto.admin.supplier.AdminSupplierListResponse(
            s.userId,
            s.companyName,
            s.contactName,
            s.contactEmail,
            s.country,
            s.contactPhone,
            s.createdAt
        )
        FROM SupplierProfile s
    """)
    Page<AdminSupplierListResponse> findAdminSuppliers(Pageable pageable);
}