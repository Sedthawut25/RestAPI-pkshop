package com.pkshop.domain.b2b.repository;

import com.pkshop.domain.b2b.entity.PurchaseOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PurchaseOrderItemRepository extends JpaRepository<PurchaseOrderItem, Long> {

    long countByPurchaseOrder_Id(Long purchaseOrderId);

    List<PurchaseOrderItem> findByPurchaseOrder_Id(Long purchaseOrderId);

    @Query("select count(i) from PurchaseOrderItem i where i.purchaseOrder.id = :poId")
    long countItems(@Param("poId") Long poId);
}