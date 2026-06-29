package com.pkshop.service.b2b;

import com.pkshop.domain.b2b.entity.PurchaseOrder;
import com.pkshop.domain.b2b.entity.PurchaseOrderItem;
import com.pkshop.domain.b2b.entity.SupplierQuotation;
import com.pkshop.domain.b2b.entity.SupplierQuotationItem;
import com.pkshop.domain.b2b.repository.PurchaseOrderItemRepository;
import com.pkshop.domain.b2b.repository.PurchaseOrderRepository;
import com.pkshop.domain.b2b.repository.SupplierQuotationRepository;
import com.pkshop.domain.catalog.entity.Product;
import com.pkshop.domain.catalog.repository.ProductRepository;
import com.pkshop.domain.user.entity.User;
import com.pkshop.domain.user.repository.UserRepository;
import com.pkshop.dto.admin.b2b.AddPoItemRequest;
import com.pkshop.dto.admin.b2b.CreatePoRequest;
import com.pkshop.dto.supplier.b2b.CreateQuotationRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
public class B2bService {

    private final SupplierQuotationRepository quotationRepo;
    private final PurchaseOrderRepository poRepo;
    private final PurchaseOrderItemRepository poItemRepo;
    private final ProductRepository productRepo;
    private final UserRepository userRepo;

    public B2bService(
            SupplierQuotationRepository quotationRepo,
            PurchaseOrderRepository poRepo,
            PurchaseOrderItemRepository poItemRepo,
            ProductRepository productRepo,
            UserRepository userRepo
    ) {
        this.quotationRepo = quotationRepo;
        this.poRepo = poRepo;
        this.poItemRepo = poItemRepo;
        this.productRepo = productRepo;
        this.userRepo = userRepo;
    }

    // -------------------------------
    // PO FLOW (Admin)
    // -------------------------------

    @Transactional
    public PurchaseOrder createPo(CreatePoRequest req, User adminUser) {
        Long supplierUserId = req.supplierUserId(); // ✅ record accessor
        String currency = (req.currency() == null || req.currency().isBlank())
                ? "THB"
                : req.currency().trim().toUpperCase();

        User supplierUser = userRepo.findById(supplierUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Supplier not found"));

        PurchaseOrder po = new PurchaseOrder();
        po.setAdminUser(adminUser);
        po.setSupplierUser(supplierUser);
        po.setCurrency(currency);
        po.setStatus("DRAFT");

        if (po.getCreatedAt() == null) po.setCreatedAt(Instant.now());
        if (po.getPoNumber() == null || po.getPoNumber().isBlank()) {
            po.setPoNumber("PO-" + System.currentTimeMillis());
        }

        return poRepo.save(po);
    }

    @Transactional
    public PurchaseOrderItem addPoItem(Long poId, AddPoItemRequest req) {
        PurchaseOrder po = poRepo.findById(poId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "PO not found"));

        if (!"DRAFT".equalsIgnoreCase(po.getStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "PO must be DRAFT to add items");
        }

        Long productId = req.productId();
        Integer qty = req.qty();
        BigDecimal targetUnitCost = req.targetUnitCost();

        if (qty == null || qty <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "qty must be > 0");
        }

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        PurchaseOrderItem item = new PurchaseOrderItem();
        item.setPurchaseOrder(po);
        item.setProduct(product);
        item.setQty(qty);
        item.setTargetUnitCost(targetUnitCost);

        if (item.getCreatedAt() == null) item.setCreatedAt(Instant.now());

        return poItemRepo.save(item);
    }

    @Transactional
    public PurchaseOrder sendPo(Long poId) {
        PurchaseOrder po = poRepo.findById(poId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "PO not found"));

        if (!"DRAFT".equalsIgnoreCase(po.getStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "PO must be DRAFT to send");
        }

        long countItems = poItemRepo.countByPurchaseOrder_Id(poId);
        if (countItems <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "PO has no items");
        }

        po.setStatus("SENT");
        return poRepo.save(po);
    }

    // -------------------------------
    // Supplier submits quotation
    // -------------------------------

    @Transactional
    public SupplierQuotation createQuotation(Long poId, CreateQuotationRequest req, User supplierUser) {
        PurchaseOrder po = poRepo.findById(poId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "PO not found"));

        if (!"SENT".equalsIgnoreCase(po.getStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "PO must be SENT to submit quotation");
        }

        if (!po.getSupplierUser().getId().equals(supplierUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not your PO");
        }

        if (req.items() == null || req.items().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quotation items required");
        }

        SupplierQuotation q = new SupplierQuotation();
        q.setPurchaseOrder(po);
        q.setSupplierUser(supplierUser);
        q.setQuotationNumber("Q-" + System.currentTimeMillis());
        q.setStatus("SUBMITTED");
        q.setValidUntil(req.validUntil());
        q.setCreatedAt(Instant.now());

        // สร้าง items
        for (CreateQuotationRequest.Item it : req.items()) {
            Product product = productRepo.findById(it.productId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found: " + it.productId()));

            SupplierQuotationItem qi = new SupplierQuotationItem();
            qi.setSupplierQuotation(q);
            qi.setProduct(product);
            qi.setQty(it.qty());
            qi.setQuotedUnitCost(it.quotedUnitCost());
            qi.setLeadTimeDays(it.leadTimeDays());
            qi.setCreatedAt(Instant.now());

            q.getItems().add(qi);
        }

        SupplierQuotation saved = quotationRepo.save(q);

        // หลัง supplier submit => PO เป็น QUOTED
        po.setStatus("QUOTED");
        poRepo.save(po);

        return saved;
    }

    // -------------------------------
    // Admin decision
    // -------------------------------

    @Transactional
    public SupplierQuotation acceptQuotation(Long quotationId) {
        SupplierQuotation q = quotationRepo.findByIdWithItems(quotationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quotation not found"));

        PurchaseOrder po = q.getPurchaseOrder();

        if (!"SUBMITTED".equalsIgnoreCase(q.getStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quotation is not SUBMITTED");
        }
        if (!"QUOTED".equalsIgnoreCase(po.getStatus()) && !"SENT".equalsIgnoreCase(po.getStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "PO status not allowed for accept");
        }

        q.setStatus("ACCEPTED");
        quotationRepo.save(q);

        quotationRepo.rejectOthers(po.getId(), q.getId());

        po.setStatus("APPROVED");
        poRepo.save(po);

        return q;
    }

    @Transactional
    public SupplierQuotation rejectQuotation(Long quotationId) {
        SupplierQuotation q = quotationRepo.findByIdWithItems(quotationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quotation not found"));

        if (!"SUBMITTED".equalsIgnoreCase(q.getStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quotation is not SUBMITTED");
        }

        q.setStatus("REJECTED");
        return quotationRepo.save(q);
    }

    @Transactional
    public SupplierQuotation decideQuotation(Long poId, Long quotationId, String action) {
        PurchaseOrder po = poRepo.findById(poId).orElseThrow();

        SupplierQuotation q = quotationRepo
                .findByIdAndPurchaseOrderId(quotationId, poId)
                .orElseThrow();

        String act = (action == null ? "" : action.trim().toUpperCase());

        if (!act.equals("ACCEPT") && !act.equals("REJECT")) {
            throw new IllegalArgumentException("action must be ACCEPT or REJECT");
        }

        if (act.equals("REJECT")) {
            q.setStatus("REJECTED");
            return quotationRepo.save(q);
        }

        q.setStatus("ACCEPTED");
        quotationRepo.save(q);


        List<SupplierQuotation> others = quotationRepo.findByPurchaseOrderIdAndIdNot(poId, quotationId);
        for (SupplierQuotation other : others) {
            if (!"REJECTED".equalsIgnoreCase(other.getStatus())) {
                other.setStatus("REJECTED");
            }
        }
        quotationRepo.saveAll(others);

        po.setStatus("CONFIRMED");
        poRepo.save(po);

        return q;
    }
}