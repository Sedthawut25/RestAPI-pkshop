package com.pkshop.domain.sales.repository;

import com.pkshop.domain.sales.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Override
    @EntityGraph(attributePaths = {"items", "items.product"})
    Page<Order> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"items", "items.product"})
    Page<Order> findByStatus(String status, Pageable pageable);

    @EntityGraph(attributePaths = {"items", "items.product"})
    Page<Order> findByOrderNumberContainingIgnoreCase(String keyword, Pageable pageable);

    @EntityGraph(attributePaths = {"items", "items.product"})
    Page<Order> findByStatusAndOrderNumberContainingIgnoreCase(String status, String keyword, Pageable pageable);

    @EntityGraph(attributePaths = {"items", "items.product"})
    Optional<Order> findByOrderNumber(String orderNumber);

    Page<Order> findByCustomerId(Long customerId, Pageable pageable);
    Page<Order> findByCustomerIdAndStatus(Long customerId, String status, Pageable pageable);
    List<Order> findByCustomerIdOrderByIdDesc(Long customerId);
    @EntityGraph(attributePaths = {"items", "items.product"})
    List<Order> findByStatusAndCreatedAtBefore(String status, LocalDateTime cutoffTime);

    @Query("""
    SELECT o
     FROM Order o
WHERE o.customer.id = :userId
AND (
    o.status = 'PAID'
    OR o.status = 'PACKING'
    OR o.status = 'SHIPPED'
)
ORDER BY o.id DESC
""")
    List<Order> findActiveOrders(Long userId);

    @Query("""
SELECT o
FROM Order o
WHERE o.customer.id = :userId
AND (
    o.status = 'DELIVERED'
    OR o.status = 'CANCELLED'
)
ORDER BY o.id DESC
""")
    List<Order> findHistoryOrders(Long userId);
}