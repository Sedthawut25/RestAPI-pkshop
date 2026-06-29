package com.pkshop.domain.catalog.repository;

import com.pkshop.domain.catalog.entity.ProductFitment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductFitmentRepository extends JpaRepository<ProductFitment, Long> {

    List<ProductFitment> findByProduct_Id(Long productId);

    Optional<ProductFitment> findByIdAndProduct_Id(Long id, Long productId);

    void deleteByIdAndProduct_Id(Long id, Long productId);
}