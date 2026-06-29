package com.pkshop.api.admin.b2b;

import com.pkshop.common.response.ApiResponse;
import com.pkshop.domain.b2b.entity.PurchaseOrder;
import com.pkshop.domain.b2b.entity.PurchaseOrderItem;
import com.pkshop.domain.b2b.entity.SupplierQuotation;
import com.pkshop.domain.b2b.repository.PurchaseOrderItemRepository;
import com.pkshop.domain.b2b.repository.PurchaseOrderRepository;
import com.pkshop.domain.b2b.repository.SupplierQuotationRepository;
import com.pkshop.domain.user.entity.User;
import com.pkshop.domain.user.repository.UserRepository;
import com.pkshop.dto.admin.b2b.AddPoItemRequest;
import com.pkshop.dto.admin.b2b.CreatePoRequest;
import com.pkshop.dto.admin.b2b.DecideQuotationRequest;
import com.pkshop.service.b2b.B2bService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/po")
public class AdminPoController {

    private final B2bService b2bService;
    private final UserRepository userRepo;
    private final PurchaseOrderRepository poRepo;
    private final PurchaseOrderItemRepository poItemRepo;
    private final SupplierQuotationRepository quotationRepository;

    public AdminPoController(
            B2bService b2bService,
            UserRepository userRepo,
            PurchaseOrderRepository poRepo,
            PurchaseOrderItemRepository poItemRepo,
            SupplierQuotationRepository quotationRepository
    ) {
        this.b2bService = b2bService;
        this.userRepo = userRepo;
        this.poRepo = poRepo;
        this.poItemRepo = poItemRepo;
        this.quotationRepository = quotationRepository;
    }

    private User currentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @PostMapping
    public ApiResponse<PurchaseOrder> create(@Valid @RequestBody CreatePoRequest req) {
        return ApiResponse.ok("Created PO", b2bService.createPo(req, currentUser()));
    }

    @PostMapping("/{poId}/items")
    public ApiResponse<PurchaseOrderItem> addItem(@PathVariable Long poId,
                                                  @Valid @RequestBody AddPoItemRequest req) {
        return ApiResponse.ok("Added PO item", b2bService.addPoItem(poId, req));
    }

    @PostMapping("/{poId}/send")
    public ApiResponse<PurchaseOrder> send(@PathVariable Long poId) {
        return ApiResponse.ok("Sent PO", b2bService.sendPo(poId));
    }

    @GetMapping("/{poId}")
    public ApiResponse<Map<String, Object>> get(@PathVariable Long poId) {
        PurchaseOrder po = poRepo.findById(poId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "PO not found"));

        List<PurchaseOrderItem> items = poItemRepo.findByPurchaseOrder_Id(poId);

        Map<String, Object> data = new HashMap<>();
        data.put("po", po);
        data.put("items", items);
        return ApiResponse.ok("PO detail", data);
    }

    @GetMapping
    public ApiResponse<List<PurchaseOrder>> list(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword
    ) {
        boolean hasStatus = status != null && !status.isBlank();
        boolean hasKeyword = keyword != null && !keyword.isBlank();

        if (hasStatus && hasKeyword) {
            return ApiResponse.ok(
                    "PO list",
                    poRepo.findByStatusAndPoNumberContainingIgnoreCase(
                            status.trim().toUpperCase(),
                            keyword.trim()
                    )
            );
        }
        if (hasStatus) {
            return ApiResponse.ok("PO list", poRepo.findByStatus(status.trim().toUpperCase()));
        }
        if (hasKeyword) {
            return ApiResponse.ok("PO list", poRepo.findByPoNumberContainingIgnoreCase(keyword.trim()));
        }

        return ApiResponse.ok("PO list", poRepo.findAll());
    }

    @GetMapping("/{poId}/quotations")
    public ApiResponse<List<SupplierQuotation>> getQuotations(@PathVariable Long poId) {
        if (!poRepo.existsById(poId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "PO not found");
        }
        return ApiResponse.ok("Quotations", quotationRepository.findByPurchaseOrderIdWithItems(poId));
    }

    @GetMapping("/{poId}/quotations/{quotationId}")
    public ApiResponse<SupplierQuotation> getQuotationDetail(
            @PathVariable Long poId,
            @PathVariable Long quotationId
    ) {
        SupplierQuotation q = quotationRepository
                .findByIdAndPurchaseOrderId(quotationId, poId)
                .orElseThrow();
        return ApiResponse.ok("Quotation detail", q);
    }

    @PostMapping("/{poId}/quotations/{quotationId}/decision")
    public ApiResponse<SupplierQuotation> decideQuotation(
            @PathVariable Long poId,
            @PathVariable Long quotationId,
            @Valid @RequestBody DecideQuotationRequest req
    ) {
        return ApiResponse.ok("Quotation updated",
                b2bService.decideQuotation(poId, quotationId, req.action()));
    }
}