package com.pkshop.domain.b2b.repository;

import com.pkshop.domain.b2b.entity.SupplierQuotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SupplierQuotationRepository extends JpaRepository<SupplierQuotation, Long> {

    @Query("""
        select distinct q
        from SupplierQuotation q
        left join fetch q.items qi
        left join fetch qi.product p
        where q.purchaseOrder.id = :poId
        order by q.id desc
    """)
    List<SupplierQuotation> findByPurchaseOrderIdWithItems(@Param("poId") Long poId);

    // ✅ เพิ่มอันนี้
    @Query("""
        select q
        from SupplierQuotation q
        left join fetch q.items qi
        left join fetch qi.product p
        where q.id = :id
    """)
    Optional<SupplierQuotation> findByIdWithItems(@Param("id") Long id);

    // ✅ เพิ่มอันนี้
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        update SupplierQuotation q
        set q.status = 'REJECTED'
        where q.purchaseOrder.id = :poId
          and q.id <> :acceptedId
          and upper(q.status) = 'SUBMITTED'
    """)
    int rejectOthers(@Param("poId") Long poId, @Param("acceptedId") Long acceptedId);

    List<SupplierQuotation> findByPurchaseOrder_Id(Long poId);

    List<SupplierQuotation> findByPurchaseOrderId(Long purchaseOrderId);
    Optional<SupplierQuotation> findByIdAndPurchaseOrderId(Long quotationId, Long purchaseOrderId);

    List<SupplierQuotation> findByPurchaseOrderIdAndIdNot(Long purchaseOrderId, Long id);
}