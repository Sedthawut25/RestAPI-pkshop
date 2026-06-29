package com.pkshop.domain.sales.repository;

import com.pkshop.domain.sales.entity.Cart;
import com.pkshop.domain.sales.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // ✅ ใช้ในหน้า cart (ดึงสินค้า + cart)
    @Query("""
        select ci
        from CartItem ci
        join fetch ci.product p
        join fetch ci.cart c
        join c.customer cu
        where cu.id = :customerId
          and c.status = :status
        order by ci.id desc
    """)
    List<CartItem> findMyCartItems(
            @Param("customerId") Long customerId,
            @Param("status") Cart.Status status
    );

    // ✅ ใช้ในขั้น checkout / create order (เหมือนข้างบน แต่ชื่อชัดเจน)
    @Query("""
        select ci
        from CartItem ci
        join fetch ci.product p
        join fetch ci.cart c
        join c.customer cu
        where cu.id = :customerId
          and c.status = :status
        order by ci.id desc
    """)
    List<CartItem> findMyCartItemsForCheckout(
            @Param("customerId") Long customerId,
            @Param("status") Cart.Status status
    );

    @Query("""
        select ci
        from CartItem ci
        join fetch ci.product p
        join fetch ci.cart c
        join c.customer cu
        where cu.id = :customerId
          and c.status = :status
          and p.id = :productId
    """)
    Optional<CartItem> findMyItem(
            @Param("customerId") Long customerId,
            @Param("status") Cart.Status status,
            @Param("productId") Long productId
    );

    // ✅ delete ต้องเป็น Modifying
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        delete from CartItem ci
        where ci.cart.id = :cartId
    """)
    int deleteByCartId(@Param("cartId") Long cartId);
}