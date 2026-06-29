package com.pkshop.domain.b2b.repository;

import com.pkshop.domain.b2b.entity.SupplierQuotationItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupplierQuotationItemRepository extends JpaRepository<SupplierQuotationItem, Long> {
    List<SupplierQuotationItem> findBySupplierQuotationId(Long supplierQuotationId);
}
