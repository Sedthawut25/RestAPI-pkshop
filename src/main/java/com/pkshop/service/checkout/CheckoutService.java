/*package com.pkshop.service.checkout;

import com.pkshop.common.exception.BadRequestException;
import com.pkshop.domain.sales.entity.Order;
import com.pkshop.domain.sales.entity.Payment;
import com.pkshop.domain.sales.repository.OrderRepository;
import com.pkshop.domain.sales.repository.PaymentRepository;
import com.pkshop.dto.customer.checkout.CreatePaymentIntentRequest;
import com.pkshop.dto.customer.checkout.CreatePaymentIntentResponse;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;

@Service
public class CheckoutService {

    private final OrderRepository orderRepo;
    private final PaymentRepository paymentRepo;

    public CheckoutService(OrderRepository orderRepo, PaymentRepository paymentRepo) {
        this.orderRepo = orderRepo;
        this.paymentRepo = paymentRepo;
    }

    private static long toMinor(BigDecimal amount) {
        return amount.multiply(BigDecimal.valueOf(100)).longValueExact();
    }

    @Transactional
    public CreatePaymentIntentResponse createPaymentIntent(CreatePaymentIntentRequest req) throws Exception {
        Order order = orderRepo.findById(req.orderId()).orElseThrow();

        if (!"PENDING_PAYMENT".equals(order.getStatus()) && !"PENDING_PAYMENT".equalsIgnoreCase(order.getStatus())) {
            if ("PAID".equalsIgnoreCase(order.getStatus())) {
                throw new BadRequestException("Order already paid");
            }
        }

        String currency = (req.currency() == null || req.currency().isBlank()) ? "thb" : req.currency().toLowerCase();

        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount(toMinor(order.getGrandTotal()))
                        .setCurrency(currency)
                        .setAutomaticPaymentMethods(
                                PaymentIntentCreateParams.AutomaticPaymentMethods.builder().setEnabled(true).build()
                        )
                        .putMetadata("orderId", String.valueOf(order.getId()))
                        .putMetadata("orderNumber", order.getOrderNumber())
                        .build();

        PaymentIntent pi = PaymentIntent.create(params);

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setProvider("STRIPE");
        payment.setStatus(pi.getStatus().toUpperCase());
        payment.setAmount(order.getGrandTotal());
        payment.setCurrency(currency);
        payment.setStripePaymentIntentId(pi.getId());
        payment.setCreatedAt(Instant.now());
        payment.setUpdatedAt(Instant.now());
        paymentRepo.save(payment);

        order.setStatus("PENDING_PAYMENT");
        orderRepo.save(order);

        return new CreatePaymentIntentResponse(pi.getClientSecret(), pi.getId());
    }
}*/
