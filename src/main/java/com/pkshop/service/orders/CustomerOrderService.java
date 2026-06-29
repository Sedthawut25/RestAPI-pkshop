package com.pkshop.service.orders;

import com.pkshop.common.exception.BadRequestException;
import com.pkshop.domain.catalog.repository.ProductRepository;
import com.pkshop.domain.catalog.entity.Product;
import com.pkshop.domain.catalog.repository.ProductRepository;
import com.pkshop.domain.sales.entity.Order;
import com.pkshop.domain.sales.entity.OrderItem;
import com.pkshop.domain.sales.repository.OrderItemRepository;
import com.pkshop.domain.sales.repository.OrderRepository;
import com.pkshop.domain.sales.repository.ShipmentRepository;
import com.pkshop.domain.user.entity.User;
import com.pkshop.dto.customer.orders.CreateOrderRequest;
import com.pkshop.dto.customer.orders.OrderDetailResponse;
import com.pkshop.dto.customer.orders.OrderListItemResponse;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;


@Service
public class CustomerOrderService {

    private final OrderRepository orderRepo;
    private final OrderItemRepository orderItemRepo;
    private final ShipmentRepository shipmentRepo;
    private final ProductRepository productRepo;

    public CustomerOrderService(
            OrderRepository orderRepo,
            OrderItemRepository orderItemRepo,
            ShipmentRepository shipmentRepo,
            ProductRepository productRepo
    ) {
        this.orderRepo = orderRepo;
        this.orderItemRepo = orderItemRepo;
        this.shipmentRepo = shipmentRepo;
        this.productRepo = productRepo;
    }

    public Page<OrderListItemResponse> listMyOrders(User customer, String status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        Page<Order> orders = (status == null || status.isBlank())
                ? orderRepo.findByCustomerId(customer.getId(), pageable)
                : orderRepo.findByCustomerIdAndStatus(customer.getId(), status.trim().toUpperCase(), pageable);

        return orders.map(o -> new OrderListItemResponse(
                o.getId(),
                o.getOrderNumber(),
                o.getStatus(),
                o.getGrandTotal(),
                o.getCreatedAt()
        ));
    }

    public OrderDetailResponse getMyOrderDetail(User customer, Long orderId) {
        Order order = orderRepo.findById(orderId).orElseThrow();

        if (!order.getCustomer().getId().equals(customer.getId())) {
            throw new BadRequestException("You do not have access to this order");
        }

        var items = orderItemRepo.findByOrderId(orderId).stream()
                .map(i -> new OrderDetailResponse.Item(
                        i.getProduct().getId(),
                        i.getProduct().getName(),
                        i.getQty(),
                        i.getUnitPrice(),
                        i.getLineTotal()
                ))
                .toList();

        var shipment = shipmentRepo.findByOrderId(orderId)
                .map(s -> new OrderDetailResponse.ShipmentInfo(
                        s.getCarrier(),
                        s.getTrackingNo(),
                        s.getStatus(),
                        s.getShippedAt(),
                        s.getDeliveredAt()
                ))
                .orElse(null);

        return new OrderDetailResponse(
                order.getId(),
                order.getOrderNumber(),
                order.getStatus(),
                order.getSubtotal(),
                order.getShippingFee(),
                order.getPromotionDiscount(),
                order.getGrandTotal(),
                order.getCreatedAt(),
                items,
                shipment,
                order.getShippingProvider(),
                order.getTrackingNumber(),
                order.getShippingAddress()

        );
    }

    public List<Order> getMyHistoryOrders(User customer) {
        return orderRepo.findHistoryOrders(customer.getId());

    }

    public List<Order> getMyActiveOrders(User customer) {
        return orderRepo.findActiveOrders(customer.getId());

    }
}