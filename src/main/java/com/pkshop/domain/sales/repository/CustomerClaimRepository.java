package com.pkshop.domain.sales.repository;

import com.pkshop.domain.sales.entity.CustomerClaim;
import com.pkshop.domain.user.entity.User;
import com.stripe.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerClaimRepository extends JpaRepository<CustomerClaim, Long> {

    List<CustomerClaim> findByCustomerOrderByCreatedAtDesc(User customer);

    List<CustomerClaim> findByCustomerIdOrderByCreatedAtDesc(Long customerId);
}
