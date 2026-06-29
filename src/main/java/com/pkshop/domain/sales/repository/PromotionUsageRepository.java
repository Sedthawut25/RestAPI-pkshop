package com.pkshop.domain.sales.repository;

import com.pkshop.domain.promotion.entity.Promotion;
import com.pkshop.domain.sales.entity.PromotionUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionUsageRepository extends JpaRepository<PromotionUsage, Long> {

    long countByCustomer_IdAndPromotion(Long customerId, Promotion promotion);

    long countByPromotion(Promotion promotion);

    void deleteByCustomer_IdAndPromotion(Long customerId, Promotion promotion);
}