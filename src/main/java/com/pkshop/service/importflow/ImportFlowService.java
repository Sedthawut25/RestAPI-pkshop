package com.pkshop.service.importflow;

import com.pkshop.common.exception.BadRequestException;
import com.pkshop.domain.b2b.entity.PurchaseOrder;
import com.pkshop.domain.b2b.entity.SupplierQuotation;
import com.pkshop.domain.b2b.repository.PurchaseOrderRepository;
import com.pkshop.domain.b2b.repository.SupplierQuotationRepository;
import com.pkshop.domain.catalog.entity.Product;
import com.pkshop.domain.catalog.repository.ProductRepository;
import com.pkshop.domain.importflow.entity.*;
import com.pkshop.domain.importflow.repository.*;
import com.pkshop.domain.user.entity.User;
import com.pkshop.service.inventory.InventoryService;
import com.pkshop.dto.admin.importflow.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Service
public class ImportFlowService {

    private final PurchaseOrderRepository poRepo;
    private final SupplierQuotationRepository quoteRepo;
    private final ProductRepository productRepo;

    private final ImportLotRepository lotRepo;
    private final ImportLotItemRepository lotItemRepo;
    private final ImportDocumentRepository docRepo;
    private final CustomsReviewRepository reviewRepo;

    private final InventoryService inventoryService;

    public ImportFlowService(
            PurchaseOrderRepository poRepo,
            SupplierQuotationRepository quoteRepo,
            ProductRepository productRepo,
            ImportLotRepository lotRepo,
            ImportLotItemRepository lotItemRepo,
            ImportDocumentRepository docRepo,
            CustomsReviewRepository reviewRepo,
            InventoryService inventoryService
    ) {
        this.poRepo = poRepo;
        this.quoteRepo = quoteRepo;
        this.productRepo = productRepo;
        this.lotRepo = lotRepo;
        this.lotItemRepo = lotItemRepo;
        this.docRepo = docRepo;
        this.reviewRepo = reviewRepo;
        this.inventoryService = inventoryService;
    }

    // -------- Admin --------

    @Transactional
    public ImportLot createImportLot(CreateImportLotRequest req, User admin) {
        PurchaseOrder po = poRepo.findById(req.purchaseOrderId()).orElseThrow();

        SupplierQuotation quotation = null;
        if (req.supplierQuotationId() != null) {
            quotation = quoteRepo.findById(req.supplierQuotationId()).orElseThrow();
        }

        ImportLot lot = new ImportLot();
        lot.setLotNumber("LOT-" + System.currentTimeMillis());
        lot.setPurchaseOrder(po);
        lot.setSupplierQuotation(quotation);
        lot.setAdminUser(admin);

        lot.setOriginCountry(req.originCountry());
        lot.setShippingMethod(req.shippingMethod().toUpperCase());

        lot.setFreightCost(req.freightCost());
        lot.setInsuranceCost(req.insuranceCost());
        lot.setCustomsDutyCost(req.customsDutyCost());
        lot.setOtherCost(req.otherCost());

        lot.setTotalImportCost(req.freightCost()
                .add(req.insuranceCost())
                .add(req.customsDutyCost())
                .add(req.otherCost()));

        lot.setStatus("DRAFT");
        lot.setCreatedAt(Instant.now());

        return lotRepo.save(lot);
    }

    @Transactional
    public ImportLotItem addLotItem(Long lotId, AddImportLotItemRequest req) {
        ImportLot lot = lotRepo.findById(lotId).orElseThrow();
        if (!"DRAFT".equals(lot.getStatus())) {
            throw new BadRequestException("Lot must be DRAFT to add items");
        }

        Product product = productRepo.findById(req.productId()).orElseThrow();

        ImportLotItem item = new ImportLotItem();
        item.setImportLot(lot);
        item.setProduct(product);
        item.setQty(req.qty());
        item.setUnitCost(req.unitCost());
        item.setLineCost(req.unitCost().multiply(BigDecimal.valueOf(req.qty())));
        item.setCreatedAt(Instant.now());

        return lotItemRepo.save(item);
    }

    @Transactional
    public ImportDocument createImportDocument(Long lotId, CreateImportDocumentRequest req, User admin) {
        ImportLot lot = lotRepo.findById(lotId).orElseThrow();
        List<ImportLotItem> items = lotItemRepo.findByImportLotId(lotId);
        if (items.isEmpty()) {
            throw new BadRequestException("Lot has no items");
        }

        ImportDocument doc = new ImportDocument();
        doc.setImportLot(lot);
        doc.setDocNumber("DOC-" + System.currentTimeMillis());
        doc.setDocType(req.docType().toUpperCase());
        doc.setStatus("SENT"); // ส่งเข้าระบบแล้ว
        doc.setSubmittedBy(admin);
        doc.setSubmittedAt(Instant.now());
        docRepo.save(doc);

        lot.setStatus("DOC_SENT");
        lotRepo.save(lot);

        return doc;
    }

    @Transactional
    public ImportDocument submitToCustoms(Long docId) {
        ImportDocument doc = docRepo.findById(docId).orElseThrow();
        if (!"SENT".equals(doc.getStatus())) {
            throw new BadRequestException("Document must be SENT to submit");
        }
        doc.setStatus("UNDER_REVIEW");
        docRepo.save(doc);

        ImportLot lot = doc.getImportLot();
        lot.setStatus("CUSTOMS_PENDING");
        lotRepo.save(lot);

        return doc;
    }

    // -------- Customs --------

    @Transactional
    public void customsApprove(Long docId, String comment, User customsUser) {
        ImportDocument doc = docRepo.findById(docId).orElseThrow();
        if (!"UNDER_REVIEW".equals(doc.getStatus())) {
            throw new BadRequestException("Document must be UNDER_REVIEW");
        }

        doc.setStatus("APPROVED");
        docRepo.save(doc);

        CustomsReview review = new CustomsReview();
        review.setImportDocument(doc);
        review.setCustomsUser(customsUser);
        review.setAction("APPROVE");
        review.setComment(comment);
        review.setReviewedAt(Instant.now());
        reviewRepo.save(review);

        ImportLot lot = doc.getImportLot();
        lot.setStatus("CUSTOMS_APPROVED");
        lotRepo.save(lot);
    }

    @Transactional
    public void customsReject(Long docId, String comment, User customsUser) {
        ImportDocument doc = docRepo.findById(docId).orElseThrow();
        if (!"UNDER_REVIEW".equals(doc.getStatus())) {
            throw new BadRequestException("Document must be UNDER_REVIEW");
        }

        doc.setStatus("REJECTED");
        docRepo.save(doc);

        CustomsReview review = new CustomsReview();
        review.setImportDocument(doc);
        review.setCustomsUser(customsUser);
        review.setAction("REJECT");
        review.setComment(comment);
        review.setReviewedAt(Instant.now());
        reviewRepo.save(review);

        ImportLot lot = doc.getImportLot();
        lot.setStatus("CUSTOMS_REJECTED");
        lotRepo.save(lot);
    }

    // -------- Admin Receive (Stock In) --------

    @Transactional
    public void receiveImportLot(Long lotId, LocalDate arrivalDate, User admin) {
        ImportLot lot = lotRepo.findById(lotId).orElseThrow();

        if (!"CUSTOMS_APPROVED".equals(lot.getStatus())) {
            throw new BadRequestException("Lot must be CUSTOMS_APPROVED to receive stock");
        }

        List<ImportLotItem> items = lotItemRepo.findByImportLotId(lotId);
        if (items.isEmpty()) {
            throw new BadRequestException("Lot has no items");
        }

        lot.setArrivalDate(arrivalDate);
        lot.setStatus("RECEIVED");
        lotRepo.save(lot);

        for (ImportLotItem item : items) {
            inventoryService.addStockFromImport(
                    item.getProduct().getId(),
                    item.getQty(),
                    item.getUnitCost(),
                    lot.getId(),
                    admin
            );
        }
    }
}
