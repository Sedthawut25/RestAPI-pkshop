package com.pkshop.domain.sales.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pkshop.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders")
@Data
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false)
    @JoinColumn(name="customer_user_id")
    private User customer;

    @Column(name="order_number", nullable=false, unique=true)
    private String orderNumber;

    @Column(nullable=false)
    private String status;

    @Column(name = "payment_intent_id", length = 255)
    private String paymentIntentId;

    @Column(name="subtotal", nullable=false)
    private BigDecimal subtotal;

    @Column(name="shipping_fee", nullable=false)
    private BigDecimal shippingFee;

    @Column(name="promotion_discount", nullable=false)
    private BigDecimal promotionDiscount;

    @Column(name="grand_total", nullable=false)
    private BigDecimal grandTotal;

    @Column(name="created_at", nullable=false)
    private LocalDateTime createdAt;

    @Column(name = "promotion_code_snapshot", length = 50)
    private String promotionCodeSnapshot;

    @Column(name = "promotion_id")
    private Long promotionId;

    @Column(name = "shipping_provider")
    private String shippingProvider;

    @Column(name = "tracking_number")
    private String trackingNumber;

    public String getShippingProvider() {
        return shippingProvider;
    }

    public void setShippingProvider(String shippingProvider) {
        this.shippingProvider = shippingProvider;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<OrderItem> items;

    private String shippingAddress;


}
