package com.pkshop.domain.sales.repository;

import com.pkshop.domain.promotion.entity.Promotion;
import com.pkshop.domain.sales.entity.PromotionUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PromotionUsageRepository extends JpaRepository<PromotionUsage, Long> {

    long countByCustomer_IdAndPromotion_Id(Long customerId, Long promotionId);

    long countByPromotion_Id(Long promotionId);

    void deleteByCustomer_IdAndPromotion(Long customerId, Promotion promotion);

    @Modifying
    @Transactional
    void deleteByOrder_Id(Long orderId);

}