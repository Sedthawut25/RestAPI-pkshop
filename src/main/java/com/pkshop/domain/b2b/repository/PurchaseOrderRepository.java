package com.pkshop.domain.b2b.repository;

import com.pkshop.domain.b2b.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {

    // ===== Admin list (ของเดิม) =====
    List<PurchaseOrder> findByStatus(String status);
    List<PurchaseOrder> findByPoNumberContainingIgnoreCase(String keyword);
    List<PurchaseOrder> findByStatusAndPoNumberContainingIgnoreCase(String status, String keyword);

    // ===== Supplier list/detail (สำคัญ: fetch join กัน Lazy) =====

    @Query("""
        select po
        from PurchaseOrder po
        join fetch po.adminUser au
        join fetch po.supplierUser su
        where su.id = :supplierId
        order by po.id desc
    """)
    List<PurchaseOrder> findBySupplierUserIdWithAdmin(@Param("supplierId") Long supplierId);

    @Query("""
        select po
        from PurchaseOrder po
        join fetch po.adminUser au
        join fetch po.supplierUser su
        where su.id = :supplierId
          and upper(po.status) = :status
        order by po.id desc
    """)
    List<PurchaseOrder> findBySupplierUserIdAndStatusWithAdmin(
            @Param("supplierId") Long supplierId,
            @Param("status") String status
    );

    @Query("""
        select po
        from PurchaseOrder po
        join fetch po.adminUser au
        join fetch po.supplierUser su
        where po.id = :poId
    """)
    Optional<PurchaseOrder> findByIdWithAdminAndSupplier(@Param("poId") Long poId);
}