package com.pkshop.api.supplier.b2b;

import com.pkshop.common.response.ApiResponse;
import com.pkshop.domain.b2b.entity.PurchaseOrder;
import com.pkshop.domain.b2b.repository.PurchaseOrderItemRepository;
import com.pkshop.domain.b2b.repository.PurchaseOrderRepository;
import com.pkshop.domain.b2b.repository.SupplierQuotationRepository;
import com.pkshop.domain.user.entity.User;
import com.pkshop.domain.user.repository.UserRepository;
import com.pkshop.dto.supplier.b2b.CreateQuotationRequest;
import com.pkshop.dto.supplier.b2b.SupplierPoDetailResponse;
import com.pkshop.dto.supplier.b2b.SupplierPoListItemResponse;
import com.pkshop.dto.supplier.b2b.SupplierQuotationResponse;
import com.pkshop.service.b2b.B2bService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/supplier/po")
public class SupplierPoController {

    private final PurchaseOrderRepository poRepo;
    private final PurchaseOrderItemRepository poItemRepo;
    private final SupplierQuotationRepository quotationRepo;
    private final UserRepository userRepo;
    private final B2bService b2bService;

    public SupplierPoController(
            PurchaseOrderRepository poRepo,
            PurchaseOrderItemRepository poItemRepo,
            SupplierQuotationRepository quotationRepo,
            UserRepository userRepo,
            B2bService b2bService
    ) {
        this.poRepo = poRepo;
        this.poItemRepo = poItemRepo;
        this.quotationRepo = quotationRepo;
        this.userRepo = userRepo;
        this.b2bService = b2bService;
    }

    private User currentUser() {
        return (User)  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private PurchaseOrder requireOwnedPo(Long poId) {
        User supplier = currentUser();
        PurchaseOrder po = poRepo.findByIdWithAdminAndSupplier(poId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "PO not found"));

        if (po.getSupplierUser() == null || po.getSupplierUser().getId() == null
                || !po.getSupplierUser().getId().equals(supplier.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not your PO");
        }
        return po;
    }

    @GetMapping
    public ApiResponse<List<SupplierPoListItemResponse>> list(@RequestParam(required = false) String status) {
        User supplier = currentUser();

        List<PurchaseOrder> list = (status == null || status.isBlank())
                ? poRepo.findBySupplierUserIdWithAdmin(supplier.getId())
                : poRepo.findBySupplierUserIdAndStatusWithAdmin(supplier.getId(), status.trim().toUpperCase());

        var mapped = list.stream().map(po -> {
            var admin = po.getAdminUser();

            // subtotal = sum(qty * targetUnitCost)
            var items = poItemRepo.findByPurchaseOrder_Id(po.getId());
            BigDecimal subtotal = items.stream()
                    .map(it -> {
                        BigDecimal unit = it.getTargetUnitCost() == null ? BigDecimal.ZERO : it.getTargetUnitCost();
                        int qty = it.getQty() == null ? 0 : it.getQty();
                        return unit.multiply(BigDecimal.valueOf(qty));
                    })
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            return new SupplierPoListItemResponse(
                    po.getId(),
                    po.getPoNumber(),
                    po.getStatus(),
                    po.getCurrency(),
                    po.getCreatedAt(),
                    admin != null ? admin.getId() : null,
                    admin != null ? admin.getEmail() : null,
                    admin != null ? admin.getFullName() : null,
                    subtotal
            );
        }).toList();

        return ApiResponse.ok("PO list", mapped);
    }

    @GetMapping("/{poId}")
    public ApiResponse<SupplierPoDetailResponse> detail(@PathVariable Long poId) {
        PurchaseOrder po = requireOwnedPo(poId);

        var mappedItems = poItemRepo.findByPurchaseOrder_Id(poId).stream().map(it -> {
            var p = it.getProduct();
            BigDecimal unit = it.getTargetUnitCost() == null ? BigDecimal.ZERO : it.getTargetUnitCost();
            int qty = it.getQty() == null ? 0 : it.getQty();
            BigDecimal line = unit.multiply(BigDecimal.valueOf(qty));

            return new SupplierPoDetailResponse.Item(
                    it.getId(),
                    p != null ? p.getId() : null,
                    p != null ? p.getSku() : null,
                    p != null ? p.getName() : null,
                    it.getQty(),
                    it.getTargetUnitCost(),
                    line
            );
        }).toList();

        BigDecimal subtotal = mappedItems.stream()
                .map(SupplierPoDetailResponse.Item::lineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        var admin = po.getAdminUser();
        var res = new SupplierPoDetailResponse(
                po.getId(),
                po.getPoNumber(),
                po.getStatus(),
                po.getCurrency(),
                po.getCreatedAt(),
                new SupplierPoDetailResponse.AdminInfo(
                        admin != null ? admin.getId() : null,
                        admin != null ? admin.getEmail() : null,
                        admin != null ? admin.getFullName() : null
                ),
                mappedItems,
                subtotal
        );

        return ApiResponse.ok("PO detail", res);
    }

    @PostMapping("/{poId}/quotation")
    public ApiResponse<SupplierQuotationResponse> createQuotation(
            @PathVariable Long poId,
            @Valid @RequestBody CreateQuotationRequest req
    ) {
        requireOwnedPo(poId);

        var q = b2bService.createQuotation(poId, req, currentUser());

        var mappedItems = q.getItems().stream().map(it -> {
            BigDecimal unit = it.getQuotedUnitCost() == null ? BigDecimal.ZERO : it.getQuotedUnitCost();
            int qty = it.getQty() == null ? 0 : it.getQty();
            BigDecimal line = unit.multiply(BigDecimal.valueOf(qty));

            return new SupplierQuotationResponse.Item(
                    it.getId(),
                    it.getProduct() != null ? it.getProduct().getId() : null,
                    it.getProduct() != null ? it.getProduct().getName() : null,
                    qty,
                    unit,
                    it.getLeadTimeDays(),
                    line
            );
        }).toList();

        BigDecimal total = mappedItems.stream()
                .map(SupplierQuotationResponse.Item::lineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        var res = new SupplierQuotationResponse(
                q.getId(),
                q.getQuotationNumber(),
                q.getStatus(),
                q.getValidUntil(),
                q.getCreatedAt(),
                q.getPurchaseOrder() != null ? q.getPurchaseOrder().getId() : null,
                mappedItems,
                total
        );

        return ApiResponse.ok("Quotation submitted", res);
    }

    @GetMapping("/{poId}/quotations")
    public ApiResponse<List<SupplierQuotationResponse>> getQuotations(@PathVariable Long poId) {
        requireOwnedPo(poId);

        var list = quotationRepo.findByPurchaseOrderIdWithItems(poId).stream().map(q -> {
            var mappedItems = q.getItems().stream().map(it -> {
                BigDecimal unit = it.getQuotedUnitCost() == null ? BigDecimal.ZERO : it.getQuotedUnitCost();
                int qty = it.getQty() == null ? 0 : it.getQty();
                BigDecimal line = unit.multiply(BigDecimal.valueOf(qty));

                return new SupplierQuotationResponse.Item(
                        it.getId(),
                        it.getProduct() != null ? it.getProduct().getId() : null,
                        it.getProduct() != null ? it.getProduct().getName() : null,
                        qty,
                        unit,
                        it.getLeadTimeDays(),
                        line
                );
            }).toList();

            BigDecimal total = mappedItems.stream()
                    .map(SupplierQuotationResponse.Item::lineTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            return new SupplierQuotationResponse(
                    q.getId(),
                    q.getQuotationNumber(),
                    q.getStatus(),
                    q.getValidUntil(),
                    q.getCreatedAt(),
                    q.getPurchaseOrder() != null ? q.getPurchaseOrder().getId() : null,
                    mappedItems,
                    total
            );
        }).toList();

        return ApiResponse.ok("Quotations", list);
    }
}