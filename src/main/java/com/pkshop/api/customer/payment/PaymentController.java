package com.pkshop.api.customer.payment;

import com.pkshop.domain.sales.entity.Order;
import com.pkshop.domain.sales.repository.OrderRepository;
import com.pkshop.service.customer.checkout.StripeCheckoutService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customer/payment")
public class PaymentController {

    private final OrderRepository orderRepository;
    private final StripeCheckoutService stripeCheckoutService;

        public PaymentController(OrderRepository orderRepository, StripeCheckoutService stripeCheckoutService) {
            this.orderRepository = orderRepository;
            this.stripeCheckoutService = stripeCheckoutService;
        }

        @PostMapping("/checkout")
        public String checkout(@RequestParam Long orderId) throws Exception {
            Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("ไม่พบคำสั่งซื้อ"));

            if (!"PENDING_PAYMENT".equals(order.getStatus())) {
                throw new RuntimeException("คำสั่งซื้อไม่อยู่ในสถานะที่จ่ายเงินได้");
            }


            return stripeCheckoutService.createCheckoutSession(order);
        }
}
