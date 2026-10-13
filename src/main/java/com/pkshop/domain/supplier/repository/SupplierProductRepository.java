package com.pkshop.domain.supplier.repository;

import com.pkshop.domain.supplier.entity.SupplierProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SupplierProductRepository extends JpaRepository<SupplierProduct, Long> {
    List<SupplierProduct> findBySupplierUser_IdAndActiveTrueOrderByProduct_NameAsc(Long supplierUserId);

    Optional<SupplierProduct> findBySupplierUser_IdAndProduct_Id(Long supplierUserId, Long productId);

    Optional<SupplierProduct> findBySupplierUser_IdAndProduct_IdAndActiveTrue(Long supplierUserId, Long productId);
}