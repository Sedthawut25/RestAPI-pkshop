package com.pkshop.domain.sales.repository;

import com.pkshop.domain.sales.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    // ✅ ใช้ property traversal: customer.id
    Optional<Cart> findByCustomerIdAndStatus(Long customerId, Cart.Status status);
}
