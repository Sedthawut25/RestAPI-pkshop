package com.pkshop.domain.promotion.repository;

import com.pkshop.domain.promotion.entity.PromotionCategory;
import com.pkshop.domain.promotion.entity.PromotionCategoryId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PromotionCategoryRepository extends JpaRepository<PromotionCategory, PromotionCategoryId> {

    // ใช้แบบนี้อ่านง่าย + ชัวร์ (อิงจาก field "promotion")
    List<PromotionCategory> findByPromotion_Id(Long promotionId);

    void deleteByPromotion_Id(Long promotionId);

    // เผื่อโค้ดเก่าเรียกชื่อเดิมไว้ (ไม่พัง)
    default List<PromotionCategory> findByPromotionId(Long promotionId) {
        return findByPromotion_Id(promotionId);
    }

    default void deleteByPromotionId(Long promotionId) {
        deleteByPromotion_Id(promotionId);
    }
}