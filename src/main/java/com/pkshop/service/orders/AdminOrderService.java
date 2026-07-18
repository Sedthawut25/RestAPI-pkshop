package com.pkshop.service.orders;

import com.pkshop.common.exception.BadRequestException;
import com.pkshop.domain.catalog.entity.Product;
import com.pkshop.domain.catalog.repository.ProductRepository;
import com.pkshop.domain.sales.entity.Order;
import com.pkshop.domain.sales.entity.OrderItem;
import com.pkshop.domain.sales.entity.Shipment;
import com.pkshop.domain.sales.repository.OrderItemRepository;
import com.pkshop.domain.sales.repository.OrderRepository;
import com.pkshop.domain.sales.repository.ShipmentRepository;
import com.pkshop.dto.admin.orders.*;
import com.pkshop.service.customer.checkout.StripeCheckoutService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AdminOrderService {

    private final OrderRepository orderRepo;
    private final OrderItemRepository orderItemRepo;
    private final ShipmentRepository shipmentRepo;
    private final ProductRepository productRepo;
    private final StripeCheckoutService checkoutService;

    public AdminOrderService(
            OrderRepository orderRepo,
            OrderItemRepository orderItemRepo,
            ShipmentRepository shipmentRepo,
            ProductRepository productRepo,
            StripeCheckoutService checkoutService
    ) {
        this.orderRepo = orderRepo;
        this.orderItemRepo = orderItemRepo;
        this.shipmentRepo = shipmentRepo;
        this.productRepo = productRepo;
        this.checkoutService = checkoutService;
    }

    public Page<AdminOrderListItemResponse> list(
            String status,
            String keyword,
            int page,
            int size
    ) {

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        Sort.by(Sort.Direction.DESC, "id")
                );

        boolean hasStatus =
                status != null && !status.isBlank();

        boolean hasKeyword =
                keyword != null && !keyword.isBlank();

        Page<Order> orders;

        if (hasStatus && hasKeyword) {

            orders =
                    orderRepo.findByStatusAndOrderNumberContainingIgnoreCase(
                            status.trim().toUpperCase(),
                            keyword.trim(),
                            pageable
                    );

        } else if (hasStatus) {

            orders =
                    orderRepo.findByStatus(
                            status.trim().toUpperCase(),
                            pageable
                    );

        } else if (hasKeyword) {

            orders =
                    orderRepo.findByOrderNumberContainingIgnoreCase(
                            keyword.trim(),
                            pageable
                    );

        } else {

            orders = orderRepo.findAll(pageable);
        }

        return orders.map(o ->
                new AdminOrderListItemResponse(
                        o.getId(),
                        o.getOrderNumber(),
                        o.getStatus(),
                        o.getGrandTotal(),
                        o.getCreatedAt(),
                        o.getCustomer().getId(),
                        o.getCustomer().getEmail(),
                        o.getCustomer().getFullName()
                )
        );
    }

    public AdminOrderDetailResponse detail(Long orderId) {

        Order order =
                orderRepo.findById(orderId)
                        .orElseThrow();

        var items =
                orderItemRepo.findByOrderId(orderId)
                        .stream()
                        .map(i ->
                                new AdminOrderDetailResponse.Item(
                                        i.getProduct().getId(),
                                        i.getProduct().getName(),
                                        i.getQty(),
                                        i.getUnitPrice(),
                                        i.getLineTotal()
                                )
                        )
                        .toList();

        var shipment =
                shipmentRepo.findByOrderId(orderId)
                        .map(s ->
                                new AdminOrderDetailResponse.ShipmentInfo(
                                        s.getCarrier(),
                                        s.getTrackingNo(),
                                        s.getStatus(),
                                        s.getShippedAt(),
                                        s.getDeliveredAt()
                                )
                        )
                        .orElse(null);

        return new AdminOrderDetailResponse(
                order.getId(),
                order.getOrderNumber(),
                order.getStatus(),
                order.getSubtotal(),

                order.getShippingFee() != null
                        ? order.getShippingFee()
                        : BigDecimal.ZERO,

                order.getPromotionDiscount() != null
                        ? order.getPromotionDiscount()
                        : BigDecimal.ZERO,

                order.getGrandTotal(),
                order.getCreatedAt(),

                new AdminOrderDetailResponse.CustomerInfo(
                        order.getCustomer().getId(),
                        order.getCustomer().getEmail()
                ),

                items,
                shipment,

                order.getShippingProvider(),
                order.getTrackingNumber()
        );
    }

    @Transactional
    public Order updateOrderStatus(
            Long orderId,
            String newStatus
    ) {

        Order order =
                orderRepo.findById(orderId)
                        .orElseThrow();

        String s =
                newStatus.trim().toUpperCase();

        if ("CANCELLED".equalsIgnoreCase(order.getStatus())) {
            throw new BadRequestException("ออเดอร์นี้ถูกยกเลิกไปเรียบร้อยแล้วครับ");
        }

        switch (s) {

            case "PACKING" -> {

                if (!"PAID".equalsIgnoreCase(order.getStatus())) {
                    throw new BadRequestException(
                            "Order must be PAID to PACK"
                    );
                }

                order.setStatus("PACKING");
            }

            case "SHIPPED" -> {

                if (
                        !"PACKING".equalsIgnoreCase(order.getStatus())
                                &&
                                !"PAID".equalsIgnoreCase(order.getStatus())
                ) {
                    throw new BadRequestException(
                            "Order must be PAID/PACKING to SHIP"
                    );
                }

                order.setStatus("SHIPPED");

                shipmentRepo.findByOrderId(orderId)
                        .orElseGet(() -> {

                            Shipment sh = new Shipment();

                            sh.setOrder(order);
                            sh.setStatus("SHIPPED");
                            sh.setShippedAt(
                                    java.time.LocalDateTime.now()
                            );

                            return shipmentRepo.save(sh);
                        });
            }

            case "DELIVERED" -> {

                if (!"SHIPPED".equalsIgnoreCase(order.getStatus())) {
                    throw new BadRequestException(
                            "Order must be SHIPPED to DELIVER"
                    );
                }

                order.setStatus("DELIVERED");

                Shipment sh =
                        shipmentRepo.findByOrderId(orderId)
                                .orElseThrow(() ->
                                        new BadRequestException(
                                                "Shipment not found"
                                        )
                                );

                sh.setStatus("DELIVERED");

                if (sh.getDeliveredAt() == null) {
                    sh.setDeliveredAt(
                            java.time.LocalDateTime.now()
                    );
                }

                shipmentRepo.save(sh);
            }

            case "CANCELLED" -> {

                if (
                        "SHIPPED".equalsIgnoreCase(order.getStatus())
                                ||
                                "DELIVERED".equalsIgnoreCase(order.getStatus())
                ) {
                    throw new BadRequestException(
                            "Cannot cancel shipped/delivered order"
                    );
                }

                if (order.getPaymentIntentId() != null && !order.getPaymentIntentId().isBlank()) {
                    try {
                        System.out.println("========== ADMIN CANCEL ORDER ==========");
                        System.out.println("กำลังดำเนินการดีดเงินคืนให้ Order รหัส: " + order.getOrderNumber());
                        checkoutService.refundOrder(order.getPaymentIntentId(),null);
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new BadRequestException("Stripe ผิดพลาด ไม่สามารถส่งเงินคืนได้: " + e.getMessage());
                    }
                }

                List<OrderItem> items = orderItemRepo.findByOrderId(orderId);
                for (OrderItem item : items) {
                    Product product = item.getProduct();
                    if (product != null) {
                        product.setStockQty(product.getStockQty() + item.getQty());
                        productRepo.save(product);
                        System.out.println("ดีดคืนสต๊อกสำเร็จ -> " + product.getName() + " จำนวน +" + item.getQty() + " ชิ้น");
                    }
                }

                order.setStatus("CANCELLED");
                System.out.println("อัปเดตสถานะออเดอร์ " + order.getOrderNumber() + " เป็น CANCELLED เรียบร้อยอย่างสมบูรณ์!");
            }

            default ->
                    throw new BadRequestException(
                            "Unsupported status: " + s
                    );
        }

        return orderRepo.save(order);
    }

    @Transactional
    public Shipment upsertShipment(
            Long orderId,
            UpsertShipmentRequest req
    ) {

        Order order =
                orderRepo.findById(orderId)
                        .orElseThrow();

        Shipment sh =
                shipmentRepo.findByOrderId(orderId)
                        .orElseGet(() -> {

                            Shipment s = new Shipment();

                            s.setOrder(order);
                            s.setStatus("PENDING");

                            return s;
                        });

        if (req.carrier() != null) {
            sh.setCarrier(req.carrier());
        }

        if (req.trackingNo() != null) {
            sh.setTrackingNo(req.trackingNo());
        }

        if (req.shippedAt() != null) {
            sh.setShippedAt(req.shippedAt());
        }

        if (req.deliveredAt() != null) {
            sh.setDeliveredAt(req.deliveredAt());
        }

        if (req.status() != null && !req.status().isBlank()) {

            String ss =
                    req.status()
                            .trim()
                            .toUpperCase();

            if (
                    !ss.equals("PENDING")
                            &&
                            !ss.equals("SHIPPED")
                            &&
                            !ss.equals("DELIVERED")
            ) {
                throw new BadRequestException(
                        "Invalid shipment status"
                );
            }

            sh.setStatus(ss);
        }

        Shipment savedShipment =
                shipmentRepo.save(sh);

        // IMPORTANT SYNC TO ORDER
        order.setShippingProvider(
                savedShipment.getCarrier()
        );

        order.setTrackingNumber(
                savedShipment.getTrackingNo()
        );

        if ("DELIVERED".equalsIgnoreCase(savedShipment.getStatus())) {

            order.setStatus("DELIVERED");

        } else if ("SHIPPED".equalsIgnoreCase(savedShipment.getStatus())) {

            order.setStatus("SHIPPED");

        } else {

            if (!"DELIVERED".equalsIgnoreCase(order.getStatus())) {
                order.setStatus("PACKING");
            }
        }

        orderRepo.save(order);

        return savedShipment;
    }
}