package com.pkshop.domain.sales.repository;

import com.pkshop.domain.sales.entity.OutOfStockRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutOfStockRequestRepository extends JpaRepository<OutOfStockRequest,Long> {

    List<OutOfStockRequest> findByCustomer_IdOrderByCreatedAtDesc(Long customerId);

    List<OutOfStockRequest> findByStatusOrderByCreatedAtDesc(String status);
}
