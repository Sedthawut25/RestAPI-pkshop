package com.pkshop.service.shop;

import com.pkshop.domain.catalog.repository.ProductRepository;
import com.pkshop.domain.catalog.repository.PromotionRepository;
import com.pkshop.domain.sales.entity.Order;
import com.pkshop.domain.sales.repository.OrderRepository;
import com.pkshop.domain.sales.repository.PromotionUsageRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderCleanupService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final PromotionUsageRepository promotionUsageRepository;
    private final PromotionRepository promotionRepository;

    public OrderCleanupService(OrderRepository orderRepository, ProductRepository productRepository, PromotionUsageRepository promotionUsageRepository, PromotionRepository promotionRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.promotionUsageRepository = promotionUsageRepository;
        this.promotionRepository = promotionRepository;
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void cancelExpiredOrdersAndRestoreStock() {
        LocalDateTime twoMinutesAgo = LocalDateTime.now().minusMinutes(2);

        List<Order> expiredOrders = orderRepository.findByStatusAndCreatedAtBefore("PENDING_PAYMENT", twoMinutesAgo);

        if(expiredOrders.isEmpty()) {
            return;
        }
        System.out.println("🔥 พบออเดอร์หมดอายุจำนวน " + expiredOrders.size() + " รายการ กำลังดำเนินการคืนสต็อก...");

        for (Order order : expiredOrders) {
            order.setStatus("CANCELLED");

            order.getItems().forEach(item -> {
                var product = item.getProduct();
                if (product != null) {
                    int restoredStock = product.getStockQty() + item.getQty();
                    product.setStockQty(restoredStock);

                    productRepository.save(product);
                    System.out.println("📦 คืนสต็อกสินค้า SKU: " + product.getSku() + " จำนวน " + item.getQty() + " ชิ้น เรียบร้อยแล้ว (ออเดอร์: " + order.getOrderNumber() + ")");
                }
            });

            if (order.getPromotionId() != null && order.getCustomer() != null) {

                promotionRepository.findById(order.getPromotionId()).ifPresent(promotion -> {
                    promotionUsageRepository.deleteByCustomer_IdAndPromotion(order.getCustomer().getId(), promotion);
                    System.out.println("คืนสิทธิ์โค้ดส่วนลด: ["+ promotion.getCode() + "] ให้ลูกค้า" + order.getCustomer().getEmail() + "เรียบร้อยแล้ว");
                });
            }

            orderRepository.save(order);
        }
    }
}