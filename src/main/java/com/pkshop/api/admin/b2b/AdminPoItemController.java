package com.pkshop.api.admin.b2b;

import com.pkshop.common.response.ApiResponse;
import com.pkshop.domain.b2b.entity.PurchaseOrder;
import com.pkshop.domain.b2b.entity.PurchaseOrderItem;
import com.pkshop.domain.b2b.repository.PurchaseOrderItemRepository;
import com.pkshop.domain.b2b.repository.PurchaseOrderRepository;
import com.pkshop.domain.catalog.entity.Product;
import com.pkshop.domain.catalog.repository.ProductRepository;
import com.pkshop.dto.admin.b2b.AdminPoDetailResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/admin/po/{poId}")
public class AdminPoItemController {

    private final PurchaseOrderRepository poRepo;
    private final PurchaseOrderItemRepository itemRepo;
    private final ProductRepository productRepo;

    public AdminPoItemController(PurchaseOrderRepository poRepo,
                                 PurchaseOrderItemRepository itemRepo,
                                 ProductRepository productRepo) {
        this.poRepo = poRepo;
        this.itemRepo = itemRepo;
        this.productRepo = productRepo;
    }

    @GetMapping("/items")
    public ApiResponse<List<PurchaseOrderItem>> items(@PathVariable Long poId) {
        poRepo.findById(poId).orElseThrow();
        return ApiResponse.ok("Items", itemRepo.findByPurchaseOrder_Id(poId));
    }

    @PostMapping("/item")
    public ApiResponse<PurchaseOrderItem> addItem(@PathVariable Long poId,
                                                  @Valid @RequestBody AddPoItemRequest req) {
        PurchaseOrder po = poRepo.findById(poId).orElseThrow();
        Product p = productRepo.findById(req.productId()).orElseThrow();

        PurchaseOrderItem item = new PurchaseOrderItem();
        item.setPurchaseOrder(po);
        item.setProduct(p);

        item.setQty(req.qty());
        // map field ให้ตรงกับ entity: targetUnitCost
        item.setTargetUnitCost(req.unitCost() == null ? p.getImportCostAvg() : req.unitCost());

        // กัน null createdAt
        if (item.getCreatedAt() == null) item.setCreatedAt(Instant.now());

        return ApiResponse.ok("Added", itemRepo.save(item));
    }

    public record AddPoItemRequest(
            @NotNull Long productId,
            @NotNull @Min(1) Integer qty,
            BigDecimal unitCost
    ) {}
}