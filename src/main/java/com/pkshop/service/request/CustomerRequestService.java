package com.pkshop.service.request;

import com.pkshop.common.exception.BadRequestException;
import com.pkshop.domain.catalog.entity.Product;
import com.pkshop.domain.catalog.repository.ProductRepository;
import com.pkshop.domain.sales.entity.OutOfStockRequest;
import com.pkshop.domain.sales.repository.OutOfStockRequestRepository;
import com.pkshop.domain.user.entity.User;
import com.pkshop.dto.customer.request.CreateRequestDto;
import com.pkshop.dto.customer.request.RequestResponse;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerRequestService {

    private final OutOfStockRequestRepository outOfStockRequestRepository;
    private final ProductRepository productRepository;

    public CustomerRequestService(OutOfStockRequestRepository outOfStockRequestRepository, ProductRepository productRepository) {
        this.outOfStockRequestRepository = outOfStockRequestRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public RequestResponse createRequest(User customer, CreateRequestDto requestDto) {
        OutOfStockRequest request = new OutOfStockRequest();
        request.setCustomer(customer);

        if (requestDto.productId() != null) {
            Product product = productRepository.findById(requestDto.productId())
                    .orElseThrow(() -> new BadRequestException("ไม่พบสินค้าในระบบ"));
            request.setProduct(product);
        }
        request.setRequestedBrandId(requestDto.requestedBrandId());
        request.setRequestedModelId(requestDto.requestModelId());
        request.setYear(requestDto.year());
        request.setDescription(requestDto.description());
        request.setImageUrl(requestDto.imageUrl());
        request.setPartName(requestDto.partName());
        request.setCarBrand(requestDto.carBrand());
        request.setCarModel(requestDto.carModel());
        request.setStatus("OPEN");

        OutOfStockRequest saved = outOfStockRequestRepository.save(request);
        return mapToResponse(saved);
    }

    public List<RequestResponse> getMyRequests(User customer) {
        return outOfStockRequestRepository.findByCustomer_IdOrderByCreatedAtDesc(customer.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private RequestResponse mapToResponse(OutOfStockRequest request) {
        Long productId = null;
        String productName = null;

        if(request.getProduct() != null) {
            productId = request.getProduct().getId();
            productName = request.getProduct().getName();
        }

        return new RequestResponse(
                request.getId(),
                productId,
                productName,
                request.getRequestedBrandId(),
                request.getRequestedModelId(),
                request.getYear(),
                request.getDescription(),
                request.getImageUrl(),
                request.getStatus(),
                request.getAdminNote(),
                request.getCreatedAt(),
                request.getPartName(),
                request.getCarBrand(),
                request.getCarModel()
        );
    }
}
