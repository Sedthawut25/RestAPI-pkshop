package com.pkshop.api.admin.catalog;

import com.pkshop.common.response.ApiResponse;
import com.pkshop.domain.catalog.entity.CarModel;
import com.pkshop.domain.catalog.entity.Product;
import com.pkshop.domain.catalog.entity.ProductFitment;
import com.pkshop.domain.catalog.repository.CarModelRepository;
import com.pkshop.domain.catalog.repository.ProductFitmentRepository;
import com.pkshop.domain.catalog.repository.ProductRepository;
import com.pkshop.dto.admin.catalog.CreateFitmentRequest;
import com.pkshop.dto.admin.catalog.FitmentResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/products/{productId}/fitments")
public class AdminProductFitmentController {

    private final ProductRepository productRepo;
    private final ProductFitmentRepository fitmentRepo;
    private final CarModelRepository modelRepo;

    public AdminProductFitmentController(ProductRepository productRepo,
                                         ProductFitmentRepository fitmentRepo,
                                         CarModelRepository modelRepo) {
        this.productRepo = productRepo;
        this.fitmentRepo = fitmentRepo;
        this.modelRepo = modelRepo;
    }

    @GetMapping
    public ApiResponse<List<FitmentResponse>> list(@PathVariable Long productId) {
        productRepo.findById(productId).orElseThrow();

        var rows = fitmentRepo.findByProduct_Id(productId).stream().map(this::toDto).toList();
        return ApiResponse.ok("Fitments", rows);
    }

    @PostMapping
    public ApiResponse<FitmentResponse> add(@PathVariable Long productId,
                                            @Valid @RequestBody CreateFitmentRequest req) {
        if (req.yearFrom() > req.yearTo()) {
            throw new IllegalArgumentException("yearFrom must be <= yearTo");
        }

        Product product = productRepo.findById(productId).orElseThrow();
        CarModel model = modelRepo.findById(req.carModelId()).orElseThrow();

        ProductFitment f = new ProductFitment();
        f.setProduct(product);
        f.setCarModel(model);

        f.setCarBrand(model.getBrand());

        f.setYearFrom(req.yearFrom());
        f.setYearTo(req.yearTo());

        return ApiResponse.ok("Created", toDto(fitmentRepo.save(f)));
    }

    @PutMapping("/{fitmentId}")
    public ApiResponse<FitmentResponse> update(@PathVariable Long productId,
                                               @PathVariable Long fitmentId,
                                               @Valid @RequestBody CreateFitmentRequest req) {
        if (req.yearFrom() > req.yearTo()) {
            throw new IllegalArgumentException("yearFrom must be <= yearTo");
        }

        ProductFitment f = fitmentRepo.findByIdAndProduct_Id(fitmentId, productId).orElseThrow();
        CarModel model = modelRepo.findById(req.carModelId()).orElseThrow();

        f.setCarModel(model);

        f.setCarBrand(model.getBrand());

        f.setYearFrom(req.yearFrom());
        f.setYearTo(req.yearTo());

        return ApiResponse.ok("Updated", toDto(fitmentRepo.save(f)));
    }

    @DeleteMapping("/{fitmentId}")
    public ApiResponse<Void> delete(@PathVariable Long productId, @PathVariable Long fitmentId) {
        fitmentRepo.deleteByIdAndProduct_Id(fitmentId, productId);
        return ApiResponse.ok("Deleted", null);
    }

    private FitmentResponse toDto(ProductFitment f) {
        return new FitmentResponse(
                f.getId(),
                f.getCarModel().getBrand().getId(),
                f.getCarModel().getBrand().getName(),
                f.getCarModel().getId(),
                f.getCarModel().getName(),
                f.getYearFrom(),
                f.getYearTo()
        );
    }
}