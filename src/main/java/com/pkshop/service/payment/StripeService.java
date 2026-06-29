/*package com.pkshop.service.payment;

import com.pkshop.domain.sales.entity.Order;
import com.stripe.Stripe;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.model.checkout.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class StripeService {

    public StripeService(@Value("${stripe.secret-key}") String secretKey) {
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
                        .setUnitAmount(order.getGrandTotal().multiply(new BigDecimal("100")).longValue())
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
                        .setSuccessUrl("http://localhost:5173/success?orderId=" + order.getId())
                        .setCancelUrl("http://localhost:5173/cancel")

                        .setPaymentIntentData(
                                SessionCreateParams.PaymentIntentData.builder()
                                        .putMetadata("orderNumber", order.getOrderNumber())
                                        .putMetadata("orderId", String.valueOf(order.getId()))
                                        .build()
                        )

                        .addLineItem(lineItem)
                        .build();

        Session session = Session.create(params);

        return session.getUrl();

    }
}*/
