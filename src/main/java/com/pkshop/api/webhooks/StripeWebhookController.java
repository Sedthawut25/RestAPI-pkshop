package com.pkshop.api.webhooks;

import com.pkshop.common.response.ApiResponse;
import com.pkshop.domain.sales.entity.Order;
import com.pkshop.domain.sales.repository.OrderRepository;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/webhooks/stripe")
@RequiredArgsConstructor
public class StripeWebhookController {

    @Value("${stripe.webhookSecret}")
    private String webhookSecret;

    private final OrderRepository orderRepo;

    @Transactional
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<Void> handle(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader
    ) {

        try {

            Event event = Webhook.constructEvent(
                    payload,
                    sigHeader,
                    webhookSecret
            );

            System.out.println("\n========== STRIPE WEBHOOK ==========");
            System.out.println("EVENT TYPE : " + event.getType());

            /*
             ================================================
             1. CHECKOUT SESSION COMPLETED (Stripe Checkout)
             ================================================
            */
            if ("checkout.session.completed".equals(event.getType())) {

                Session session = (Session) event
                        .getDataObjectDeserializer()
                        .deserializeUnsafe();

                if (session == null) {
                    System.out.println("Checkout Session is null");
                    return ApiResponse.ok("Session is null", null);
                }

                // ดึง orderNumber และกำหนดเป็น final เพื่อใช้ใน Lambda ได้อย่างถูกต้อง
                String tempOrderNumber = (session.getMetadata() != null) ? session.getMetadata().get("orderNumber") : null;
                if (tempOrderNumber == null || tempOrderNumber.isBlank()) {
                    tempOrderNumber = session.getClientReferenceId();
                }
                final String orderNumber = tempOrderNumber;

                System.out.println("CHECKOUT SESSION ID : " + session.getId());
                System.out.println("ORDER NUMBER : " + orderNumber);

                if (orderNumber == null || orderNumber.isBlank()) {
                    throw new RuntimeException("orderNumber not found in session metadata/clientReferenceId");
                }

                Order order = orderRepo.findByOrderNumber(orderNumber)
                        .orElseThrow(() -> new RuntimeException("Order not found : " + orderNumber));

                if ("PAID".equalsIgnoreCase(order.getStatus())) {
                    System.out.println("ORDER ALREADY PAID : " + order.getOrderNumber());
                    return ApiResponse.ok("Order already paid", null);
                }

                // อัปเดตสถานะเป็น PAID
                order.setStatus("PAID");
                if (session.getPaymentIntent() != null) {
                    order.setPaymentIntentId(session.getPaymentIntent());
                }
                orderRepo.save(order);

                System.out.println("✅ PAYMENT SUCCESS (checkout.session.completed)");
                System.out.println("ORDER : " + order.getOrderNumber());
                System.out.println("TOTAL : " + order.getGrandTotal());
            }

            /*
             ================================================
             2. PAYMENT INTENT SUCCEEDED (Direct PaymentIntent)
             ================================================
            */
            else if ("payment_intent.succeeded".equals(event.getType())) {

                PaymentIntent intent = (PaymentIntent) event
                        .getDataObjectDeserializer()
                        .deserializeUnsafe();

                if (intent == null) {
                    System.out.println("PaymentIntent is null");
                    return ApiResponse.ok("PaymentIntent is null", null);
                }

                System.out.println("PAYMENT INTENT ID : " + intent.getId());
                System.out.println("METADATA : " + intent.getMetadata());

                final String orderNumber = intent.getMetadata() != null ? intent.getMetadata().get("orderNumber") : null;
                System.out.println("ORDER NUMBER : " + orderNumber);

                if (orderNumber == null || orderNumber.isBlank()) {
                    throw new RuntimeException("orderNumber not found in metadata");
                }

                Order order = orderRepo.findByOrderNumber(orderNumber)
                        .orElseThrow(() -> new RuntimeException("Order not found : " + orderNumber));

                if ("PAID".equalsIgnoreCase(order.getStatus())) {
                    System.out.println("ORDER ALREADY PAID : " + order.getOrderNumber());
                    return ApiResponse.ok("Order already paid", null);
                }

                order.setStatus("PAID");
                order.setPaymentIntentId(intent.getId());
                orderRepo.save(order);

                System.out.println("✅ PAYMENT SUCCESS (payment_intent.succeeded)");
                System.out.println("ORDER : " + order.getOrderNumber());
                System.out.println("TOTAL : " + order.getGrandTotal());
            }

            /*
             ================================================
             3. PAYMENT FAILED
             ================================================
            */
            else if ("payment_intent.payment_failed".equals(event.getType())) {

                PaymentIntent intent = (PaymentIntent) event
                        .getDataObjectDeserializer()
                        .deserializeUnsafe();

                if (intent != null && intent.getMetadata() != null) {
                    final String orderNumber = intent.getMetadata().get("orderNumber");

                    if (orderNumber != null && !orderNumber.isBlank()) {
                        Order order = orderRepo.findByOrderNumber(orderNumber)
                                .orElseThrow(() -> new RuntimeException("Order not found : " + orderNumber));

                        order.setStatus("FAILED");
                        orderRepo.save(order);

                        System.out.println("❌ PAYMENT FAILED : " + order.getOrderNumber());
                    }
                }
            }

            return ApiResponse.ok("Webhook success", null);

        } catch (SignatureVerificationException e) {
            e.printStackTrace();
            throw new RuntimeException("Invalid Stripe signature", e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Webhook error : " + e.getMessage(), e);
        }
    }
}