package com.pkshop.api.customer.shop;

import com.pkshop.common.exception.BadRequestException;
import com.pkshop.common.response.ApiResponse;
import com.pkshop.domain.catalog.repository.PromotionRepository;
import com.pkshop.domain.sales.repository.PromotionUsageRepository;
import com.pkshop.domain.user.entity.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/customer/promotions")
public class CustomerPromotionController {

    private final PromotionRepository promotionRepository;
    private final PromotionUsageRepository promotionUsageRepository;

    public CustomerPromotionController(PromotionRepository promotionRepository, PromotionUsageRepository promotionUsageRepository) {
        this.promotionRepository = promotionRepository;
        this.promotionUsageRepository = promotionUsageRepository;
    }

    @GetMapping("/apply")
    public ApiResponse<?> apply(
            @RequestParam String code,
            @RequestParam BigDecimal subtotal,
            @AuthenticationPrincipal User user
    ) {

        var promotion = promotionRepository
                .findByCodeAndActiveTrue(code.toUpperCase())
                .orElseThrow(() -> new BadRequestException("ไม่พบโค้ดโปรโมชั่น"));

        LocalDateTime now = LocalDateTime.now();

        // 1. เช็ควันเริ่ม และ วันหมดอายุ (กัน NullPointerException)
        if (promotion.getStartAt() != null && now.isBefore(promotion.getStartAt())) {
            throw new BadRequestException("โปรโมชั่นยังไม่เริ่มใช้งาน");
        }
        if (promotion.getEndAt() != null && now.isAfter(promotion.getEndAt())) {
            throw new BadRequestException("โปรโมชั่นหมดอายุแล้ว");
        }

        // 2. เช็คขั้นต่ำยอดสั่งซื้อ
        if (promotion.getMinOrderAmount() != null &&
                subtotal.compareTo(promotion.getMinOrderAmount()) < 0) {
            throw new BadRequestException("ยอดสั่งซื้อไม่ถึงขั้นต่ำ");
        }

        // 3. เช็คสิทธิ์การใช้ต่อผู้ใช้
        if (promotion.getPerUserLimit() != null && promotion.getPerUserLimit() > 0){
            if (user == null) {
                throw new BadRequestException("กรุณาเข้าสู่ระบบก่อนใช้งานโค้ดส่วนลด");
            }
            long userUsageCount = promotionUsageRepository.countByCustomer_IdAndPromotion_Id(user.getId(), promotion.getId());
            if (userUsageCount >= promotion.getPerUserLimit()) {
                throw new BadRequestException("คุณใช้โค้ดส่วนลดนี้ครบตามสิทธิ์ที่กำหนดแล้วครับ");
            }
        }

        // 4. ✅ เพิ่มการเช็คสิทธิ์รวมทั้งระบบ (usageLimit)
        if (promotion.getUsageLimit() != null && promotion.getUsageLimit() > 0) {
            long totalUsageCount = promotionUsageRepository.countByPromotion_Id(promotion.getId());
            if (totalUsageCount >= promotion.getUsageLimit()) {
                throw new BadRequestException("โค้ดส่วนลดนี้ถูกใช้ครบตามจำนวนที่กำหนดแล้วครับ");
            }
        }

        BigDecimal discount = BigDecimal.ZERO;

        if ("PERCENT".equals(promotion.getPromoType())) {
            discount = subtotal.multiply(promotion.getValue()).divide(BigDecimal.valueOf(100));

            if (promotion.getMaxDiscount() != null) {
                discount = discount.min(promotion.getMaxDiscount());
            }
        }
        else if ("FIXED".equals(promotion.getPromoType())) {
            discount = promotion.getValue();
        }

        return ApiResponse.ok("ใช้โค้ดสำเร็จ", discount);
    }
}
