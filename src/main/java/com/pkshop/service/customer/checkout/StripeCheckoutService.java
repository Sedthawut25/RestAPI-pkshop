package com.pkshop.service.customer.checkout;

import com.pkshop.domain.sales.entity.Order;
import com.stripe.Stripe;
import com.stripe.model.Refund;
import com.stripe.param.RefundCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.model.checkout.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class StripeCheckoutService {

    public StripeCheckoutService(@Value("${stripe.secretKey}") String secretKey) {
        Stripe.apiKey = secretKey;
    }

    public String createCheckoutSession(Order order) throws Exception {

        SessionCreateParams.LineItem.PriceData.ProductData productData =
                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                        .setName("Order #" + order.getOrderNumber())
                        .build();

        SessionCreateParams.LineItem.PriceData priceData =
                SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency("thb")
                        .setUnitAmount(
                                order.getGrandTotal()
                                        .multiply(new BigDecimal("100"))
                                        .longValue()
                        )
                        .setProductData(productData)
                        .build();

        SessionCreateParams.LineItem lineItem =
                SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(priceData)
                        .build();

        SessionCreateParams params =
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                        .addPaymentMethodType(SessionCreateParams.PaymentMethodType.PROMPTPAY)

                        .setSuccessUrl(
                                "http://localhost:5173/success?orderId=" + order.getId()
                        )

                        .setCancelUrl(
                                "http://localhost:5173/cancel"
                        )

                        .setPaymentIntentData(
                                SessionCreateParams.PaymentIntentData.builder()
                                        .putMetadata("orderId", String.valueOf(order.getId()))
                                        .putMetadata("orderNumber", order.getOrderNumber())
                                        .build()
                        )

                        .addLineItem(lineItem)
                        .build();

        Session session = Session.create(params);

        return session.getUrl();
    }

    public void refundOrder(String paymentIntentId, BigDecimal amount) throws Exception {

        if (paymentIntentId == null || paymentIntentId.isBlank()) {
            throw new IllegalArgumentException("ไม่พบ Payment Intent ID สำหรับคืนเงิน");
        }

        RefundCreateParams params;

        if (amount != null && amount.compareTo(BigDecimal.ZERO) > 0) {

            long amountInCents = amount.multiply(new BigDecimal("100")).longValue();
            params = RefundCreateParams.builder()
                    .setPaymentIntent(paymentIntentId)
                    .setAmount(amountInCents)
                    .build();

        } else {

            params = RefundCreateParams.builder()
                    .setPaymentIntent(paymentIntentId)
                    .build();
        }
        Refund refund = Refund.create(params);

        System.out.println("\n===============Stripe Refund Success===============");
        System.out.println("Refund ID : " + refund.getId());
        System.out.println("Status : " + refund.getStatus());
    }
}

//เหลือแก้ขอคืนเงินแค่สินค้านั้นในออเดอร์
