package com.pkshop.api.webhooks;

import com.pkshop.common.response.ApiResponse;
import com.pkshop.domain.sales.entity.Order;
import com.pkshop.domain.sales.repository.OrderRepository;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
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

            if ("payment_intent.succeeded".equals(event.getType())) {

                PaymentIntent intent =
                        (PaymentIntent) event
                                .getDataObjectDeserializer()
                                .deserializeUnsafe();

                if (intent == null) {

                    System.out.println("PaymentIntent is null");

                    return ApiResponse.ok(
                            "PaymentIntent is null",
                            null
                    );
                }

                /*
                 ========================================
                 DEBUG LOG
                 ========================================
                */
                System.out.println("PAYMENT INTENT ID : " + intent.getId());

                System.out.println("METADATA : "
                        + intent.getMetadata());

                String orderNumber =
                        intent.getMetadata()
                                .get("orderNumber");

                System.out.println("ORDER NUMBER : "
                        + orderNumber);

                /*
                 ========================================
                 VALIDATE ORDER NUMBER
                 ========================================
                */
                if (orderNumber == null ||
                        orderNumber.isBlank()) {

                    throw new RuntimeException(
                            "orderNumber not found in metadata"
                    );
                }

                /*
                 ========================================
                 FIND ORDER
                 ========================================
                */
                Order order = orderRepo
                        .findByOrderNumber(orderNumber)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Order not found : "
                                                + orderNumber
                                )
                        );

                /*
                 ========================================
                 PREVENT DUPLICATE WEBHOOK
                 ========================================
                */
                if ("PAID".equalsIgnoreCase(
                        order.getStatus())) {

                    System.out.println(
                            "ORDER ALREADY PAID : "
                                    + order.getOrderNumber()
                    );

                    return ApiResponse.ok(
                            "Order already paid",
                            null
                    );
                }

                /*
                 ========================================
                 UPDATE ORDER STATUS
                 ========================================
                */
                order.setStatus("PAID");
                order.setPaymentIntentId(intent.getId());
                orderRepo.save(order);

                /*
                 ========================================
                 SUCCESS LOG
                 ========================================
                */
                System.out.println(
                        "PAYMENT SUCCESS"
                );

                System.out.println(
                        "ORDER : "
                                + order.getOrderNumber()
                );

                System.out.println(
                        "TOTAL : "
                                + order.getGrandTotal()
                );
            }

            /*
             ============================================
             PAYMENT FAILED
             ============================================
            */
            else if ("payment_intent.payment_failed"
                    .equals(event.getType())) {

                PaymentIntent intent =
                        (PaymentIntent) event
                                .getDataObjectDeserializer()
                                .deserializeUnsafe();

                if (intent != null) {

                    System.out.println(
                            "FAILED METADATA : "
                                    + intent.getMetadata()
                    );

                    String orderNumber =
                            intent.getMetadata()
                                    .get("orderNumber");

                    if (orderNumber != null &&
                            !orderNumber.isBlank()) {

                        Order order = orderRepo
                                .findByOrderNumber(orderNumber)
                                .orElseThrow(() ->
                                        new RuntimeException(
                                                "Order not found : "
                                                        + orderNumber
                                        )
                                );

                        order.setStatus("FAILED");

                        orderRepo.save(order);

                        System.out.println(
                                "PAYMENT FAILED : "
                                        + order.getOrderNumber()
                        );
                    }
                }
            }

            /*
             ============================================
             RETURN SUCCESS
             ============================================
            */
            return ApiResponse.ok(
                    "Webhook success",
                    null
            );

        } catch (SignatureVerificationException e) {

            e.printStackTrace();

            throw new RuntimeException(
                    "Invalid Stripe signature",
                    e
            );

        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException(
                    "Webhook error : "
                            + e.getMessage(),
                    e
            );

        }
    }
}