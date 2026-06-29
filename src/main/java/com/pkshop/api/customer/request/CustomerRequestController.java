package com.pkshop.api.customer.request;

import com.pkshop.common.response.ApiResponse;
import com.pkshop.domain.user.entity.User;
import com.pkshop.domain.user.repository.UserRepository;
import com.pkshop.dto.customer.request.CreateRequestDto;
import com.pkshop.dto.customer.request.RequestResponse;
import com.pkshop.service.request.CustomerRequestService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer/requests")
public class CustomerRequestController {

    private final CustomerRequestService customerRequestService;
    private final UserRepository userRepo;

    public CustomerRequestController(CustomerRequestService customerRequestService, UserRepository userRepo) {
        this.customerRequestService = customerRequestService;
        this.userRepo = userRepo;
    }

    private User currentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @GetMapping
    public ApiResponse<List<RequestResponse>> getMyRequests() {
        List<RequestResponse> data = customerRequestService.getMyRequests(currentUser());
        return ApiResponse.ok("ดึงข้อมูลสำเร็จ", data);
    }

    @PostMapping
    public ApiResponse<RequestResponse> createRequest(@RequestBody CreateRequestDto dto) {
        RequestResponse data = customerRequestService.createRequest(currentUser(), dto);
        return ApiResponse.ok("ส่งคำขอสำเร็จ แอดมินจะรีบตรวจสอบให้เร็วที่สุด", data);
    }
}