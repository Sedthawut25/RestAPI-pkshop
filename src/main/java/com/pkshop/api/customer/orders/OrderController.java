/*package com.pkshop.api.customer.orders;

import com.pkshop.common.response.ApiResponse;
import com.pkshop.domain.user.entity.User;
import com.pkshop.domain.user.repository.UserRepository;
import com.pkshop.dto.customer.orders.CreateOrderRequest;
import com.pkshop.dto.customer.orders.OrderSummaryResponse;
import com.pkshop.service.orders.OrderService;
import jakarta.validation.Valid;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserRepository userRepo;

    public OrderController(OrderService orderService, UserRepository userRepo) {
        this.orderService = orderService;
        this.userRepo = userRepo;
    }

    private User currentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepo.findByEmail(email).orElseThrow();
    }

    @PostMapping
    public ApiResponse<OrderSummaryResponse> createOrder(@Valid @RequestBody CreateOrderRequest req) {
        return ApiResponse.ok("Order created", orderService.createOrderFromCart(currentUser(), req));
    }
}
*/