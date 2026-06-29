package com.pkshop.service.promotions;

import com.pkshop.common.exception.BadRequestException;
import com.pkshop.domain.catalog.entity.Category;
import com.pkshop.domain.catalog.entity.Product;
import com.pkshop.domain.catalog.entity.PromotionProduct;
import com.pkshop.domain.catalog.repository.CategoryRepository;
import com.pkshop.domain.catalog.repository.ProductRepository;
import com.pkshop.domain.catalog.repository.PromotionRepository;
import com.pkshop.domain.promotion.entity.Promotion;
import com.pkshop.domain.promotion.entity.PromotionCategory;
import com.pkshop.domain.promotion.repository.PromotionCategoryRepository;
import com.pkshop.domain.promotion.repository.PromotionProductRepository;
import com.pkshop.domain.user.entity.User;
import com.pkshop.dto.admin.promotions.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PromotionService {

    private final PromotionRepository promoRepo;
    private final PromotionProductRepository ppRepo;
    private final PromotionCategoryRepository pcRepo;
    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;

    public PromotionService(
            PromotionRepository promoRepo,
            PromotionProductRepository ppRepo,
            PromotionCategoryRepository pcRepo,
            ProductRepository productRepo,
            CategoryRepository categoryRepo
    ) {
        this.promoRepo = promoRepo;
        this.ppRepo = ppRepo;
        this.pcRepo = pcRepo;
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
    }

    @Transactional
    public Promotion create(CreatePromotionRequest req, User admin) {
        String code = req.code().trim().toUpperCase();
        if (promoRepo.findByCode(code).isPresent()) throw new BadRequestException("Promotion code already exists");

        Promotion p = new Promotion();
        p.setCode(code);
        p.setName(req.name().trim());
        p.setDescription(req.description());
        p.setPromoType(req.promoType().trim().toUpperCase());
        p.setValue(req.value());
        p.setMaxDiscount(req.maxDiscount());
        p.setMinOrderAmount(req.minOrderAmount());
        p.setStartAt(req.startAt());
        p.setEndAt(req.endAt());
        p.setActive(req.active() == null ? Boolean.TRUE : req.active());
        p.setUsageLimit(req.usageLimit());
        p.setPerUserLimit(req.perUserLimit());
        p.setAppliesTo(req.appliesTo().trim().toUpperCase());
        p.setCreatedBy(admin);

        validatePromotion(p);
        return promoRepo.save(p);
    }

    @Transactional
    public Promotion update(Long id, UpdatePromotionRequest req) {
        Promotion p = promoRepo.findById(id).orElseThrow();

        if (req.name() != null) p.setName(req.name().trim());
        if (req.description() != null) p.setDescription(req.description());
        if (req.promoType() != null) p.setPromoType(req.promoType().trim().toUpperCase());
        if (req.value() != null) p.setValue(req.value());
        if (req.maxDiscount() != null) p.setMaxDiscount(req.maxDiscount());
        if (req.minOrderAmount() != null) p.setMinOrderAmount(req.minOrderAmount());
        if (req.startAt() != null) p.setStartAt(req.startAt());
        if (req.endAt() != null) p.setEndAt(req.endAt());
        if (req.active() != null) p.setActive(req.active());
        if (req.usageLimit() != null) p.setUsageLimit(req.usageLimit());
        if (req.perUserLimit() != null) p.setPerUserLimit(req.perUserLimit());
        if (req.appliesTo() != null) p.setAppliesTo(req.appliesTo().trim().toUpperCase());

        validatePromotion(p);
        return promoRepo.save(p);
    }

    public Promotion get(Long id) { return promoRepo.findById(id).orElseThrow(); }

    public List<Promotion> list() { return promoRepo.findAll(); }

    @Transactional
    public void delete(Long id) {
        pcRepo.deleteByPromotion_Id(id);
        ppRepo.deleteByPromotion_Id(id);
        promoRepo.deleteById(id);
    }

    @Transactional
    public void setTargets(Long promoId, PromotionTargetRequest req) {
        Promotion promo = promoRepo.findById(promoId).orElseThrow();
        String appliesTo = promo.getAppliesTo();

        ppRepo.deleteByPromotion_Id(promoId);
        pcRepo.deleteByPromotion_Id(promoId);

        if ("PRODUCT".equalsIgnoreCase(appliesTo)) {
            if (req.productIds() == null || req.productIds().isEmpty()) {
                throw new BadRequestException("PRODUCT promotion requires productIds");
            }
            for (Long pid : req.productIds()) {
                Product prod = productRepo.findById(pid).orElseThrow();
                PromotionProduct pp = new PromotionProduct();
                pp.setPromotion(promo);
                pp.setProduct(prod);
                ppRepo.save(pp);
            }
        } else if ("CATEGORY".equalsIgnoreCase(appliesTo)) {
            if (req.categoryIds() == null || req.categoryIds().isEmpty()) {
                throw new BadRequestException("CATEGORY promotion requires categoryIds");
            }
            for (Long cid : req.categoryIds()) {
                Category cat = categoryRepo.findById(cid).orElseThrow();
                PromotionCategory pc = new PromotionCategory();
                pc.setPromotion(promo);
                pc.setCategory(cat);
                pcRepo.save(pc);
            }
        }
    }

    public PromotionResponse toResponse(Promotion p) {
        List<Long> productIds = ppRepo.findByPromotion_Id(p.getId())
                .stream().map(x -> x.getProduct().getId()).toList();

        List<Long> categoryIds = pcRepo.findByPromotion_Id(p.getId())
                .stream().map(x -> x.getCategory().getId()).toList();

        return new PromotionResponse(
                p.getId(), p.getCode(), p.getName(), p.getPromoType(), p.getValue(),
                p.getMaxDiscount(), p.getMinOrderAmount(),
                p.getStartAt(), p.getEndAt(),
                p.getActive(), p.getUsageLimit(), p.getPerUserLimit(), p.getAppliesTo(),
                productIds, categoryIds
        );
    }

    private static void validatePromotion(Promotion p) {
        if (!p.getPromoType().equals("PERCENT") && !p.getPromoType().equals("FIXED")) {
            throw new BadRequestException("promoType must be PERCENT or FIXED");
        }
        if (!p.getAppliesTo().equals("ORDER") && !p.getAppliesTo().equals("PRODUCT") && !p.getAppliesTo().equals("CATEGORY")) {
            throw new BadRequestException("appliesTo must be ORDER/PRODUCT/CATEGORY");
        }
        if (p.getEndAt().isBefore(p.getStartAt())) throw new BadRequestException("endAt must be after startAt");
    }
}