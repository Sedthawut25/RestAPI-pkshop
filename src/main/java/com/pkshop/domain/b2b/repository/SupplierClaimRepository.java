package com.pkshop.domain.b2b.repository;

import com.pkshop.domain.b2b.entity.SupplierClaim;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface SupplierClaimRepository extends JpaRepository<SupplierClaim, Long> {

    @Override
    @EntityGraph(attributePaths = {"purchaseOrder", "supplierUser", "adminUser"})
    Page<SupplierClaim> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"purchaseOrder", "supplierUser", "adminUser"})
    Page<SupplierClaim> findByStatus(String status, Pageable pageable);

    @EntityGraph(attributePaths = {"purchaseOrder", "supplierUser", "adminUser"})
    List<SupplierClaim> findBySupplierUser_IdOrderByCreatedAtDesc(Long supplierUserId);

    @EntityGraph(attributePaths = {"purchaseOrder", "supplierUser", "adminUser"})
    List<SupplierClaim> findBySupplierUser_IdAndStatusOrderByCreatedAtDesc(Long supplierUserId, String status);

    @Query("""
        SELECT SUM(sc.refundAmount) 
        FROM SupplierClaim sc 
        WHERE sc.claimType = 'RETURN_REFUND' 
          AND sc.status IN ('APPROVED', 'COMPLETED') 
          AND sc.createdAt >= :from 
          AND sc.createdAt <= :to
    """)
    BigDecimal sumApprovedRefundAmount(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);
}