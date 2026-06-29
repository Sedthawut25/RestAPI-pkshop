package com.pkshop.domain.catalog.service;

import com.pkshop.domain.catalog.entity.*;
import com.pkshop.domain.catalog.repository.CarModelRepository;
import com.pkshop.domain.catalog.repository.ProductFitmentRepository;
import com.pkshop.domain.catalog.repository.ProductRepository;
import com.pkshop.dto.admin.catalog.CreateFitmentRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductFitmentService {

    private final ProductRepository productRepo;
    private final CarModelRepository carModelRepo;
    private final ProductFitmentRepository fitmentRepo;

    public ProductFitment createFitment(Long productId, CreateFitmentRequest req) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found: " + productId));

        CarModel model = carModelRepo.findById(req.carModelId())
                .orElseThrow(() -> new EntityNotFoundException("CarModel not found: " + req.carModelId()));

        if (req.yearTo() != null && req.yearTo() < req.yearFrom()) {
            throw new IllegalArgumentException("yearTo must be >= yearFrom");
        }

        ProductFitment f = new ProductFitment();
        f.setProduct(product);
        f.setCarModel(model);

        f.setCarBrand(model.getBrand());

        f.setYearFrom(req.yearFrom());
        f.setYearTo(req.yearTo());

        return fitmentRepo.save(f);
    }
}