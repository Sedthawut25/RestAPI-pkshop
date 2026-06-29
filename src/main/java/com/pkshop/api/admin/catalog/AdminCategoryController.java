package com.pkshop.api.admin.catalog;

import com.pkshop.common.response.ApiResponse;
import com.pkshop.domain.catalog.entity.Category;
import com.pkshop.domain.catalog.repository.CategoryRepository;
import com.pkshop.dto.admin.catalog.UpsertNameRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/categories")
public class AdminCategoryController {

    private final CategoryRepository repo;

    public AdminCategoryController(CategoryRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public ApiResponse<List<Category>> list() {
        return ApiResponse.ok("Categories", repo.findAll());
    }

    @PostMapping
    public ApiResponse<Category> create(@Valid @RequestBody UpsertNameRequest req) {
        if (repo.existsByNameIgnoreCase(req.name().trim())) {
            throw new IllegalArgumentException("Category name already exists");
        }
        Category c = new Category();
        c.setName(req.name().trim());
        return ApiResponse.ok("Created", repo.save(c));
    }

    @PutMapping("/{id}")
    public ApiResponse<Category> update(@PathVariable Long id, @Valid @RequestBody UpsertNameRequest req) {
        Category c = repo.findById(id).orElseThrow();
        c.setName(req.name().trim());
        return ApiResponse.ok("Updated", repo.save(c));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        repo.deleteById(id);
        return ApiResponse.ok("Deleted", null);
    }
}