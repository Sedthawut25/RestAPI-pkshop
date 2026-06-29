package com.pkshop.domain.sales.repository;

import com.pkshop.domain.sales.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByStripePaymentIntentId(String paymentIntentId);
}
