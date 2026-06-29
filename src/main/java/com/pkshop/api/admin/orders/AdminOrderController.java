package com.pkshop.api.admin.orders;

import com.pkshop.common.response.ApiResponse;
import com.pkshop.domain.sales.entity.Order;
import com.pkshop.domain.sales.entity.Shipment;
import com.pkshop.domain.sales.repository.OrderRepository;
import com.pkshop.dto.admin.orders.*;
import com.pkshop.service.orders.AdminOrderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/orders")
public class AdminOrderController {

    private final AdminOrderService adminOrderService;
    private final OrderRepository orderRepository;

    public AdminOrderController(AdminOrderService adminOrderService, OrderRepository orderRepository) {
        this.adminOrderService = adminOrderService;
        this.orderRepository = orderRepository;
    }

    @GetMapping
    public ApiResponse<Page<AdminOrderListItemResponse>> list(
            @RequestParam(defaultValue="0") int page,
            @RequestParam(defaultValue="20") int size,
            @RequestParam(required=false) String status,
            @RequestParam(required=false) String keyword
    ) {
        return ApiResponse.ok("Orders", adminOrderService.list(status, keyword, page, size));
    }

    @GetMapping("/{orderId}")
    public ApiResponse<AdminOrderDetailResponse> detail(@PathVariable Long orderId) {
        return ApiResponse.ok("Order", adminOrderService.detail(orderId));
    }

    @PutMapping("/{orderId}/status")
    public ApiResponse<Order> updateStatus(@PathVariable Long orderId, @Valid @RequestBody UpdateOrderStatusRequest req) {
        return ApiResponse.ok("Updated", adminOrderService.updateOrderStatus(orderId, req.status()));
    }

    @PutMapping("/{orderId}/shipment")
    public ApiResponse<Shipment> upsertShipment(@PathVariable Long orderId, @RequestBody UpsertShipmentRequest req) {
        return ApiResponse.ok("Shipment updated", adminOrderService.upsertShipment(orderId, req));
    }

    @PutMapping("/{id}/shipping")
    public ApiResponse<Void> updateShipping(
            @PathVariable Long id,
            @RequestBody UpdateShippingRequest req
    ) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setShippingProvider(req.shippingProvider());
        order.setTrackingNumber(req.trackingNumber());

        order.setStatus("SHIPPED");

        orderRepository.save(order);

        return ApiResponse.ok("Updated shipping", null);
    }
}