package com.pkshop.domain.catalog.repository;

import com.pkshop.domain.promotion.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    Optional<Promotion> findByCode(String code);

    // ✅ แก้จาก IsActive -> Active
    Optional<Promotion> findByCodeAndActiveTrue(String code);
}