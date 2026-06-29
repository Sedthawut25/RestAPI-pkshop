package com.pkshop.domain.user.repository;

import com.pkshop.domain.user.entity.CustomerProfile;
import com.pkshop.dto.admin.customer.AdminCustomerListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CustomerProfileRepository
        extends JpaRepository<CustomerProfile, Long> {

    @Query("""
    SELECT new com.pkshop.dto.admin.customer.AdminCustomerListResponse(
        u.id,
        u.fullName,
        u.email,
        u.phone,
        cp.points,
        u.status,
        u.lastLoginAt,
        cp.createdAt
    )
    FROM CustomerProfile cp
    JOIN cp.user u
    ORDER BY cp.createdAt DESC
    """)
    Page<AdminCustomerListResponse> findAdminCustomers(
            Pageable pageable
    );
}