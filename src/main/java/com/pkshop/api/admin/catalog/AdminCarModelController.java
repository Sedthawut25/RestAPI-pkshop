package com.pkshop.api.admin.catalog;

import com.pkshop.common.response.ApiResponse;
import com.pkshop.domain.catalog.entity.CarBrand;
import com.pkshop.domain.catalog.entity.CarModel;
import com.pkshop.domain.catalog.repository.CarBrandRepository;
import com.pkshop.domain.catalog.repository.CarModelRepository;
import com.pkshop.dto.admin.catalog.UpsertCarModelRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/car-models")
public class AdminCarModelController {

    private final CarModelRepository modelRepo;
    private final CarBrandRepository brandRepo;

    public AdminCarModelController(CarModelRepository modelRepo, CarBrandRepository brandRepo) {
        this.modelRepo = modelRepo;
        this.brandRepo = brandRepo;
    }

    @GetMapping
    public ApiResponse<List<CarModel>> list(@RequestParam(required = false) Long brandId) {
        if (brandId != null) {
            return ApiResponse.ok("Car models", modelRepo.findByBrand_Id(brandId));
        }
        return ApiResponse.ok("Car models", modelRepo.findAll());
    }

    @PostMapping
    public ApiResponse<CarModel> create(@Valid @RequestBody UpsertCarModelRequest req) {
        CarBrand brand = brandRepo.findById(req.brandId()).orElseThrow();

        CarModel m = new CarModel();
        m.setBrand(brand);
        m.setName(req.name().trim());

        return ApiResponse.ok("Created", modelRepo.save(m));
    }

    @PutMapping("/{id}")
    public ApiResponse<CarModel> update(@PathVariable Long id, @Valid @RequestBody UpsertCarModelRequest req) {
        CarBrand brand = brandRepo.findById(req.brandId()).orElseThrow();
        CarModel m = modelRepo.findById(id).orElseThrow();

        m.setBrand(brand);
        m.setName(req.name().trim());

        return ApiResponse.ok("Updated", modelRepo.save(m));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        modelRepo.deleteById(id);
        return ApiResponse.ok("Deleted", null);
    }
}