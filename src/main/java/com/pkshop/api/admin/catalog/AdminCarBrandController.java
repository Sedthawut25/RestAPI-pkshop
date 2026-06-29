package com.pkshop.api.admin.catalog;

import com.pkshop.common.response.ApiResponse;
import com.pkshop.domain.catalog.entity.CarBrand;
import com.pkshop.domain.catalog.repository.CarBrandRepository;
import com.pkshop.dto.admin.catalog.UpsertNameRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/car-brands")
public class AdminCarBrandController {

    private final CarBrandRepository repo;

    public AdminCarBrandController(CarBrandRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public ApiResponse<List<CarBrand>> list() {
        return ApiResponse.ok("Car brands", repo.findAll());
    }

    @PostMapping
    public ApiResponse<CarBrand> create(@Valid @RequestBody UpsertNameRequest req) {
        if (repo.existsByNameIgnoreCase(req.name().trim())) {
            throw new IllegalArgumentException("Brand name already exists");
        }
        CarBrand b = new CarBrand();
        b.setName(req.name().trim());
        return ApiResponse.ok("Created", repo.save(b));
    }

    @PutMapping("/{id}")
    public ApiResponse<CarBrand> update(@PathVariable Long id, @Valid @RequestBody UpsertNameRequest req) {
        CarBrand b = repo.findById(id).orElseThrow();
        b.setName(req.name().trim());
        return ApiResponse.ok("Updated", repo.save(b));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        repo.deleteById(id);
        return ApiResponse.ok("Deleted", null);
    }
}