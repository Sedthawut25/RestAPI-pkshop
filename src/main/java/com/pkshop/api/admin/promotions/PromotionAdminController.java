package com.pkshop.api.admin.promotions;

import com.pkshop.common.response.ApiResponse;
import com.pkshop.domain.user.entity.User;
import com.pkshop.domain.user.repository.UserRepository;
import com.pkshop.dto.admin.promotions.*;
import com.pkshop.service.promotions.PromotionService;
import jakarta.validation.Valid;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/promotions")
public class PromotionAdminController {

    private final PromotionService promoService;
    private final UserRepository userRepo;

    public PromotionAdminController(PromotionService promoService, UserRepository userRepo) {
        this.promoService = promoService;
        this.userRepo = userRepo;
    }

    private User currentUser() {
        return  (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @PostMapping
    public ApiResponse<PromotionResponse> create(@Valid @RequestBody CreatePromotionRequest req) {
        var p = promoService.create(req, currentUser());
        return ApiResponse.ok("Created", promoService.toResponse(p));
    }

    @PutMapping("/{id}")
    public ApiResponse<PromotionResponse> update(@PathVariable Long id, @RequestBody UpdatePromotionRequest req) {
        var p = promoService.update(id, req);
        return ApiResponse.ok("Updated", promoService.toResponse(p));
    }

    @GetMapping("/{id}")
    public ApiResponse<PromotionResponse> get(@PathVariable Long id) {
        return ApiResponse.ok("Promotion", promoService.toResponse(promoService.get(id)));
    }

    @GetMapping
    public ApiResponse<List<PromotionResponse>> list() {
        return ApiResponse.ok("Promotions", promoService.list().stream().map(promoService::toResponse).toList());
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        promoService.delete(id);
        return ApiResponse.ok("Deleted", null);
    }

    @PutMapping("/{id}/targets")
    public ApiResponse<Void> setTargets(@PathVariable Long id, @RequestBody PromotionTargetRequest req) {
        promoService.setTargets(id, req);
        return ApiResponse.ok("Targets updated", null);
    }
}