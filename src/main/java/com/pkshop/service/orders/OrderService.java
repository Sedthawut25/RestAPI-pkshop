package com.pkshop.service.orders;

import com.pkshop.common.exception.BadRequestException;
import com.pkshop.domain.catalog.entity.Category;
import com.pkshop.domain.catalog.entity.Product;
import com.pkshop.domain.catalog.repository.ProductRepository;
import com.pkshop.domain.catalog.repository.PromotionRepository;
import com.pkshop.domain.promotion.entity.Promotion;
import com.pkshop.domain.promotion.repository.PromotionCategoryRepository;
import com.pkshop.domain.promotion.repository.PromotionProductRepository;
import com.pkshop.domain.sales.entity.*;
import com.pkshop.domain.sales.repository.*;
import com.pkshop.domain.user.entity.User;
import com.pkshop.dto.customer.orders.CreateOrderRequest;
import com.pkshop.dto.customer.orders.OrderSummaryResponse;

import com.pkshop.service.customer.checkout.StripeCheckoutService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepo;
    private final OrderItemRepository orderItemRepo;

    private final PromotionRepository promoRepo;
    private final PromotionProductRepository promotionProductRepo;
    private final PromotionCategoryRepository promotionCategoryRepo;
    private final PromotionUsageRepository promotionUsageRepo;
    private final ProductRepository productRepo;
    private final StripeCheckoutService checkoutService;

    public OrderService(
            OrderRepository orderRepo,
            OrderItemRepository orderItemRepo,
            PromotionRepository promoRepo,
            PromotionProductRepository promotionProductRepo,
            PromotionCategoryRepository promotionCategoryRepo,
            PromotionUsageRepository promotionUsageRepo,
            ProductRepository productRepo,
            StripeCheckoutService checkoutService
    ) {
        this.orderRepo = orderRepo;
        this.orderItemRepo = orderItemRepo;
        this.promoRepo = promoRepo;
        this.promotionProductRepo = promotionProductRepo;
        this.promotionCategoryRepo = promotionCategoryRepo;
        this.promotionUsageRepo = promotionUsageRepo;
        this.productRepo = productRepo;
        this.checkoutService = checkoutService;
    }

    private static BigDecimal nz(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }

    @Transactional
    public OrderSummaryResponse createOrderFromCart(User customer, CreateOrderRequest req) {

        System.out.println("========== CREATE ORDER ==========");
        System.out.println("REQ = " + req);

        if (customer == null) {
            throw new BadRequestException("User is required");
        }

        if (req.items() == null || req.items().isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }

        BigDecimal subtotal = BigDecimal.ZERO;

        // ===== CALCULATE SUBTOTAL =====
        for (var itemReq : req.items()) {

            Product product = productRepo.findById(itemReq.productId())
                    .orElseThrow(() -> new BadRequestException("Product not found"));

            if (product.getStockQty() < itemReq.qty()) {
                throw new BadRequestException("สินค้าไม่พอ");
            }

            BigDecimal lineTotal = product.getPrice()
                    .multiply(BigDecimal.valueOf(itemReq.qty()));

            subtotal = subtotal.add(lineTotal);
        }

        // ===== SHIPPING =====
        BigDecimal shippingFee = new BigDecimal("1500.00");

        // ===== PROMOTION =====
        BigDecimal discount = BigDecimal.ZERO;
        Promotion promo = null;

        String promoCode = req.promotionCode();

        if (promoCode != null && !promoCode.isBlank()) {

            promo = promoRepo.findByCodeAndActiveTrue(
                    promoCode.trim().toUpperCase()
            ).orElseThrow(() ->
                    new BadRequestException("Invalid promo code"));

            LocalDateTime now = LocalDateTime.now();

            if (promo.getStartAt() != null &&
                    promo.getStartAt().isAfter(now)) {
                throw new BadRequestException("Promotion not started");
            }

            if (promo.getEndAt() != null &&
                    promo.getEndAt().isBefore(now)) {
                throw new BadRequestException("Promotion expired");
            }

            if (promo.getPerUserLimit() != null && promo.getPerUserLimit() > 0) {
                long userUsageCount = promotionUsageRepo.countByCustomer_IdAndPromotion(customer.getId(), promo);
                if (userUsageCount >= promo.getPerUserLimit()) {
                    throw new BadRequestException("คุณใช้โค้ดส่วนลดนี้ครบตามสิทธิ์ที่กำหนดแล้วครับ");
                }
            }

            if (promo.getUsageLimit() != null && promo.getUsageLimit() > 0) {
                long totalUsageCount = promotionUsageRepo.countByPromotion(promo);
                if (totalUsageCount >= promo.getUsageLimit()) {
                    throw new BadRequestException("โค้ดส่วนลดนี้ถูกใช้ครบตามจำนวนที่กำหนดแล้วครับ");
                }
            }

            BigDecimal eligibleTotal =
                    calculateEligibleTotal(promo, req);

            if ("PERCENT".equalsIgnoreCase(promo.getPromoType())) {

                discount = eligibleTotal
                        .multiply(nz(promo.getValue()))
                        .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);

                if (promo.getMaxDiscount() != null) {
                    discount = discount.min(promo.getMaxDiscount());
                }

            } else if ("FIXED".equalsIgnoreCase(promo.getPromoType())) {

                discount = nz(promo.getValue());
            }

            discount = discount.min(subtotal);

            System.out.println("DISCOUNT = " + discount);
        }

        BigDecimal grandTotal =
                subtotal
                        .add(shippingFee)
                        .subtract(discount);

        Order order = new Order();

        order.setCustomer(customer);
        order.setOrderNumber("ORD-" + System.currentTimeMillis());
        order.setStatus("PENDING_PAYMENT");

        order.setSubtotal(subtotal);
        order.setShippingFee(shippingFee);
        order.setPromotionDiscount(discount);
        order.setGrandTotal(grandTotal);

        order.setShippingAddress(req.shippingAddress());
        order.setCreatedAt(LocalDateTime.now());

        if (promo != null) {
            order.setPromotionCodeSnapshot(promo.getCode());
            order.setPromotionId(promo.getId());
        }

        order = orderRepo.save(order);

        for (var itemReq : req.items()) {

            Product product = productRepo.findById(itemReq.productId())
                    .orElseThrow(() -> new BadRequestException("Product not found"));

            product.setStockQty(product.getStockQty() - itemReq.qty());

            productRepo.save(product);

            BigDecimal lineTotal = product.getPrice()
                    .multiply(BigDecimal.valueOf(itemReq.qty()));

            OrderItem item = new OrderItem();

            item.setOrder(order);
            item.setProduct(product);
            item.setQty(itemReq.qty());

            item.setUnitPrice(product.getPrice());
            item.setLineTotal(lineTotal);

            item.setProductNameSnapshot(product.getName());
            item.setProductImageSnapshot(product.getImageUrl());

            orderItemRepo.save(item);
        }

        if (promo != null) {

            PromotionUsage usage = new PromotionUsage();

            usage.setPromotion(promo);
            usage.setCustomer(customer);
            usage.setOrder(order);
            usage.setUsedAt(LocalDateTime.now());

            promotionUsageRepo.save(usage);
        }

        return new OrderSummaryResponse(
                order.getId(),
                order.getOrderNumber(),
                order.getStatus(),
                order.getSubtotal(),
                order.getShippingFee(),
                order.getPromotionDiscount(),
                order.getGrandTotal()
        );
    }

    private BigDecimal calculateEligibleTotal(
            Promotion promo,
            CreateOrderRequest req
    ) {

        String appliesTo = promo.getAppliesTo();

        if (appliesTo == null ||
                appliesTo.isBlank() ||
                "ORDER".equalsIgnoreCase(appliesTo)) {

            BigDecimal total = BigDecimal.ZERO;

            for (var itemReq : req.items()) {

                Product product = productRepo.findById(itemReq.productId())
                        .orElseThrow();

                total = total.add(
                        product.getPrice()
                                .multiply(BigDecimal.valueOf(itemReq.qty()))
                );
            }

            return total;
        }

        if ("PRODUCT".equalsIgnoreCase(appliesTo)) {

            Set<Long> promoProductIds =
                    promotionProductRepo.findByPromotion_Id(promo.getId())
                            .stream()
                            .map(pp -> pp.getProduct().getId())
                            .collect(Collectors.toSet());

            BigDecimal total = BigDecimal.ZERO;

            for (var itemReq : req.items()) {

                if (promoProductIds.contains(itemReq.productId())) {

                    Product product =
                            productRepo.findById(itemReq.productId())
                                    .orElseThrow();

                    total = total.add(
                            product.getPrice()
                                    .multiply(BigDecimal.valueOf(itemReq.qty()))
                    );
                }
            }

            return total;
        }

        if ("CATEGORY".equalsIgnoreCase(appliesTo)) {

            Set<Long> promoCategoryIds =
                    promotionCategoryRepo.findByPromotion_Id(promo.getId())
                            .stream()
                            .map(pc -> pc.getCategory().getId())
                            .collect(Collectors.toSet());

            BigDecimal total = BigDecimal.ZERO;

            for (var itemReq : req.items()) {

                Product product =
                        productRepo.findById(itemReq.productId())
                                .orElseThrow();

                Category cat = product.getCategory();

                if (cat != null &&
                        promoCategoryIds.contains(cat.getId())) {

                    total = total.add(
                            product.getPrice()
                                    .multiply(BigDecimal.valueOf(itemReq.qty()))
                    );
                }
            }

            return total;
        }

        return BigDecimal.ZERO;
    }

    @Transactional
    public void cancleOrder(Long orderId) {

        Order order = orderRepo.findById(orderId).orElseThrow(() -> new BadRequestException("ไม่พบออเดอร์นี้ในระบบ"));

        if("CANCELLED".equals(order.getStatus())) {
            throw new BadRequestException("ออเดอร์นี้ทำการกยกเลิกไปแล้ว");
        }

        if(order.getPaymentIntentId() != null && !order.getPaymentIntentId().isBlank()) {
            try {
                System.out.println("กำลังดำเนินการคืนเงินให้ Order: " + order.getOrderNumber());
                checkoutService.refundOrder(order.getPaymentIntentId());
            }
            catch (Exception e) {
                e.printStackTrace();
                throw new BadRequestException("ไม่สามารถคืนเงินได้: " + e.getMessage());
            }
        }
        order.setStatus("CANCELLED");
        orderRepo.save(order);

        System.out.println("ยกเลิกออเดอร์" + order.getOrderNumber() + "สำเร็จ" );
    }
}