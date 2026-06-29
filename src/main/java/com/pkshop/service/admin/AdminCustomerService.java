package com.pkshop.service.admin;

import com.pkshop.domain.user.repository.CustomerProfileRepository;
import com.pkshop.dto.admin.customer.AdminCustomerListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class AdminCustomerService {

    private final CustomerProfileRepository customerProfileRepository;

    public AdminCustomerService(
            CustomerProfileRepository customerProfileRepository
    ) {
        this.customerProfileRepository = customerProfileRepository;
    }

    public Page<AdminCustomerListResponse> listAdminCustomers(
            int page,
            int size
    ) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        return customerProfileRepository.findAdminCustomers(
                pageable
        );
    }
}