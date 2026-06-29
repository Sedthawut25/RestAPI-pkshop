package com.pkshop.api.customer.orders;

import com.pkshop.common.response.ApiResponse;
import com.pkshop.domain.sales.entity.Order;
import com.pkshop.domain.sales.repository.OrderRepository;
import com.pkshop.domain.user.entity.User;
import com.pkshop.domain.user.repository.UserRepository;

import com.pkshop.dto.customer.orders.CreateOrderRequest;
import com.pkshop.dto.customer.orders.OrderDetailResponse;
import com.pkshop.dto.customer.orders.OrderListItemResponse;
import com.pkshop.dto.customer.orders.OrderSummaryResponse;

import com.pkshop.service.orders.CustomerOrderService;
import com.pkshop.service.orders.OrderService;

import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer/orders")
public class CustomerOrderController {

    private final OrderService orderService;
    private final CustomerOrderService customerOrderService;

    private final UserRepository userRepo;
    private final OrderRepository orderRepo;

    public CustomerOrderController(
            OrderService orderService,
            CustomerOrderService customerOrderService,
            UserRepository userRepo,
            OrderRepository orderRepo
    ) {
        this.orderService = orderService;
        this.customerOrderService = customerOrderService;
        this.userRepo = userRepo;
        this.orderRepo = orderRepo;
    }

    private User currentUser() {
        return (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

    @GetMapping
    public ApiResponse<Page<OrderListItemResponse>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status
    ) {

        return ApiResponse.ok(
                "Orders",
                customerOrderService.listMyOrders(
                        currentUser(),
                        status,
                        page,
                        size
                )
        );
    }

    @GetMapping("/{orderId}")
    public ApiResponse<OrderDetailResponse> detail(
            @PathVariable Long orderId
    ) {

        return ApiResponse.ok(
                "Order",
                customerOrderService.getMyOrderDetail(
                        currentUser(),
                        orderId
                )
        );
    }

    @PostMapping("/{orderId}/paid")
    public ApiResponse<?> markPaid(
            @PathVariable Long orderId
    ) {

        Order order = orderRepo.findById(orderId)
                .orElseThrow(() ->
                        new RuntimeException("ไม่พบคำสั่งซื้อ"));

        order.setStatus("PAID");

        orderRepo.save(order);

        return ApiResponse.ok(
                "Order paid",
                orderId
        );
    }

    @PostMapping
    public ApiResponse<OrderSummaryResponse> createOrder(
            @RequestBody CreateOrderRequest req
    ) {

        return ApiResponse.ok(
                "Order created",
                orderService.createOrderFromCart(
                        currentUser(),
                        req
                )
        );
    }

    @GetMapping("/active")
    public ApiResponse<?> activeOrders() {
        return ApiResponse.ok(
                "active orders",
                customerOrderService.getMyActiveOrders(currentUser())
        );
    }

    @GetMapping("/history")
    public ApiResponse<?> history() {
        return ApiResponse.ok(
                "history",
                customerOrderService.getMyHistoryOrders(currentUser())
        );

    }
}