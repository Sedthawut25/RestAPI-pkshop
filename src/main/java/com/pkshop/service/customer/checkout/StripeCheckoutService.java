package com.pkshop.service.customer.checkout;

import com.pkshop.domain.sales.entity.Order;
import com.stripe.Stripe;
import com.stripe.model.Refund;
import com.stripe.model.checkout.Session;
import com.stripe.param.RefundCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.math.BigDecimal;
import java.net.URI;

@Service
public class StripeCheckoutService {

    private final String fallbackFrontendUrl;

    public StripeCheckoutService(
            @Value("${stripe.secretKey}") String secretKey,
            @Value("${app.frontend.url:http://localhost:8081}") String fallbackFrontendUrl
    ) {
        Stripe.apiKey = secretKey;
        this.fallbackFrontendUrl = fallbackFrontendUrl;
    }

    public String createCheckoutSession(Order order) throws Exception {

        String baseUrl = resolveClientBaseUrl();

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

                        .setClientReferenceId(order.getOrderNumber())
                        .putMetadata("orderId", String.valueOf(order.getId()))
                        .putMetadata("orderNumber", order.getOrderNumber())

                        .setSuccessUrl(baseUrl + "/success?orderId=" + order.getId())
                        .setCancelUrl(baseUrl + "/cancel?orderId=" + order.getId())

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

    private String resolveClientBaseUrl() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();

                // 1. ดึงจาก Origin header (Browser มาตรฐานจะส่งมาเสมอเวลา fetch/axios)
                String origin = request.getHeader("Origin");
                if (origin != null && !origin.isBlank()) {
                    return origin.replaceAll("/+$", "");
                }

                // 2. ดึงจาก Referer header (ถ้า Origin ไม่มี)
                String referer = request.getHeader("Referer");
                if (referer != null && !referer.isBlank()) {
                    URI uri = new URI(referer);
                    String portPart = (uri.getPort() != -1 && uri.getPort() != 80 && uri.getPort() != 443) ? ":" + uri.getPort() : "";
                    return uri.getScheme() + "://" + uri.getHost() + portPart;
                }
            }
        } catch (Exception e) {
            System.err.println("ไม่สามารถดึง Client Origin ได้ จะใช้ค่าเริ่มต้น: " + e.getMessage());
        }

        // 3. ถ้าไม่มี ให้ใช้ค่า fallback จาก properties
        return fallbackFrontendUrl.replaceAll("/+$", "");
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
