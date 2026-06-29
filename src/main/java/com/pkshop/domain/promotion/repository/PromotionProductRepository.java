package com.pkshop.domain.promotion.repository;

import com.pkshop.domain.catalog.entity.PromotionProduct;
import com.pkshop.domain.catalog.entity.PromotionProductId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PromotionProductRepository extends JpaRepository<PromotionProduct, PromotionProductId> {
    List<PromotionProduct> findByPromotion_Id(Long promotionId);
    void deleteByPromotion_Id(Long promotionId);
}