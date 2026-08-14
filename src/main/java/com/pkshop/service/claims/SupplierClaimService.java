package com.pkshop.service.claims;

import com.pkshop.common.exception.BadRequestException;
import com.pkshop.domain.b2b.entity.PurchaseOrder;
import com.pkshop.domain.b2b.entity.SupplierClaim;
import com.pkshop.domain.b2b.entity.SupplierClaimAttachment;
import com.pkshop.domain.b2b.repository.PurchaseOrderItemRepository;
import com.pkshop.domain.b2b.repository.PurchaseOrderRepository;
import com.pkshop.domain.b2b.repository.SupplierClaimAttachmentRepository;
import com.pkshop.domain.b2b.repository.SupplierClaimRepository;
import com.pkshop.domain.catalog.entity.Product;
import com.pkshop.domain.catalog.repository.ProductRepository;
import com.pkshop.domain.user.entity.User;
import com.pkshop.dto.admin.supplierclaim.CreateSupplierClaimRequest;
import com.pkshop.dto.admin.supplierclaim.SupplierClaimResponse;
import com.pkshop.service.inventory.InventoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SupplierClaimService {

    private final SupplierClaimRepository supplierClaimRepository;
    private final SupplierClaimAttachmentRepository supplierClaimAttachmentRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseOrderItemRepository purchaseOrderItemRepository;
    private final ProductRepository productRepository;
    private final InventoryService inventoryService;

    public SupplierClaimService( SupplierClaimRepository supplierClaimRepository, SupplierClaimAttachmentRepository supplierClaimAttachmentRepository
    , PurchaseOrderRepository purchaseOrderRepository, PurchaseOrderItemRepository purchaseOrderItemRepository, ProductRepository productRepository, InventoryService inventoryService) {
        this.supplierClaimRepository = supplierClaimRepository;
        this.supplierClaimAttachmentRepository = supplierClaimAttachmentRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.purchaseOrderItemRepository = purchaseOrderItemRepository;
        this.productRepository = productRepository;
        this.inventoryService = inventoryService;
    }

    @Transactional
    public SupplierClaimResponse createClaim(CreateSupplierClaimRequest req, User admin) {
        String type = req.claimType().trim().toUpperCase();
        if(!type.equals("RETURN_REFUND") && !type.equals("REPLACEMENT")) {
            throw new BadRequestException("Claim Type ต้องเป็น RETURN_REFUND หรือ REPLACEMENT");
        }

        PurchaseOrder po = purchaseOrderRepository.findByIdWithAdminAndSupplier(req.purchaseOrderId())
                .orElseThrow(() -> new BadRequestException("ไม่พบหมายเลขคำสั่งซื้อ หมายเลข: " + req.purchaseOrderId()));

        //ต้องเป็นสินค้าที่อยู่ในหมายเลขคำสั่งซื้อจริงๆ
        var poItem = purchaseOrderItemRepository.findByPurchaseOrder_Id(po.getId()).stream()
                .filter(it -> it.getProduct() != null && it.getProduct().getId().equals(req.productId()))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("สินค้าไม่อยู่ในหมายเลขคำสั่งซื้อที่เลือก"));

        if(req.quantity() > poItem.getQty()) {
            throw new BadRequestException("จำนวนที่เคลม ("+ req.quantity() + ") มากกว่าจำนวนที่สั่งซื้อในคำสั่งซื้อ (" + poItem.getQty() + ")");
        }

        Product product = productRepository.findById(req.productId())
                .orElseThrow(() -> new BadRequestException("ไม่พบสินค้าในระบบ"));

        BigDecimal unitCost = poItem.getTargetUnitCost() != null
                ? poItem.getTargetUnitCost()
                : product.getImportCostAvg();
        BigDecimal refundAmount = (req.refundAmount() != null && req.refundAmount().compareTo(BigDecimal.ZERO) > 0)
                ? req.refundAmount()
                : unitCost.multiply(BigDecimal.valueOf(req.quantity()));

        SupplierClaim claim = SupplierClaim.builder()
                .purchaseOrder(po)
                .supplierUser(po.getSupplierUser())
                .adminUser(admin)
                .productId(product.getId())
                .productName(product.getName())
                .quantity(req.quantity())
                .claimType(type)
                .status("PENDING")
                .description(req.description())
                .refundAmount(refundAmount)
                .build();

        claim = supplierClaimRepository.save(claim);

        // ตัดสต็อก ของที่เสียหายถูกส่งคืนซัพพลายเออร์
        inventoryService.adjustStock(
                product.getId(), -req.quantity(),
                "SUPPLIER_CLAIM_OUT", "supplier_claims", claim.getId(),
                "ส่งคืนสินค้าให้ supplier (เคลม # " + claim.getId() + ")", admin
        );

        //รูปหลักฐาน
        if (req.attachmentUrls() != null) {
            for (String url : req.attachmentUrls()) {
                if (url != null && !url.isBlank()) {
                    supplierClaimAttachmentRepository.save(SupplierClaimAttachment.builder()
                                    .supplierClaim(claim)
                                    .fileUrl(url.trim())
                            .build());
                }
            }
        }
        return toResponse(claim);
    }

    @Transactional(readOnly = true)
    public Page<SupplierClaimResponse> listClaims(String status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size , Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<SupplierClaim> claims = (status == null || status.isBlank())
                ? supplierClaimRepository.findAll(pageable)
                : supplierClaimRepository.findByStatus(status.trim().toUpperCase(), pageable);

        return claims.map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public SupplierClaimResponse getDetail(Long id) {
        return toResponse(requiredClaim(id));
    }

    @Transactional
    public SupplierClaimResponse cancelClaim(Long id, User admin) {
        SupplierClaim claim = requiredClaim(id);

        if(!"PENDING".equals(claim.getStatus())) {
            throw new BadRequestException("ยกเลิกได้เฉพาะเคลมที่สถานะ PENDING เท่านั้น");
        }

        inventoryService.adjustStock(
                claim.getProductId(), claim.getQuantity(),
                "SUPPLIER_CLAIM_RESTORE", "supplier_claims", claim.getId(),
                "ยกเลิกเคลม supplier # " + claim.getId() + "คืนสต็อก" , admin
        );


        claim.setStatus("CANCELLED");
        claim.setResolvedAt(LocalDateTime.now());
        return toResponse(supplierClaimRepository.save(claim));
    }

    @Transactional
    public SupplierClaimResponse receiveReplacement(Long id, User admin) {
        SupplierClaim claim = requiredClaim(id);

        if(!"APPROVED".equals(claim.getStatus()) || !"REPLACEMENT".equals(claim.getClaimType())) {
            throw new BadRequestException("รับสินค้าเปลี่ยนได้เฉพาะเคลมประเภท REPLACEMENT ที่สถานะ APPROVED เท่านั้น");
        }

        // สินค้าเปลี่ยนแล้ว -> บวกเพิ่มในสต็อก
        inventoryService.adjustStock(
                claim.getProductId(), claim.getQuantity(),
                "SUPPLIER_CLAIM_REPLACE_IN", "supplier_claims", claim.getId(),
                "รับสินค้าเปลี่ยนจาก supplier (เคลม # " + claim.getId() + ")", admin
        );

        claim.setStatus("COMPLETED");
        claim.setResolvedAt(LocalDateTime.now());
        return toResponse(supplierClaimRepository.save(claim));
    }

    // supplier
    @Transactional
    public List<SupplierClaimResponse> listForSupplier(User supplier, String status) {
        List<SupplierClaim> claims = (status == null || status.isBlank())
                ? supplierClaimRepository.findBySupplierUser_IdOrderByCreatedAtDesc(supplier.getId())
                : supplierClaimRepository.findBySupplierUser_IdAndStatusOrderByCreatedAtDesc(supplier.getId(), status.trim().toUpperCase());
        return claims.stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public SupplierClaimResponse getDetailForSupplier(Long id, User supplier) {
        SupplierClaim claim = requiredOwnedClaim(id, supplier);
        return toResponse(claim);
    }

    @Transactional
    public SupplierClaimResponse response(Long id, User supplier, String action, String response) {
        SupplierClaim claim = requiredOwnedClaim(id, supplier);

        if(!"PENDING".equals(claim.getStatus())) {
            throw new BadRequestException("เคลมนี้ถูกตอบกลับไปแล้ว (สถานะ: " + claim.getStatus() + ")");
        }

        String act = action == null ? "" : action.trim().toUpperCase();
        claim.setSupplierResponse(response);

        switch (act) {
            case "APPROVE", "APPROVED" -> {
                claim.setStatus("APPROVED");
                if("RETURN_REFUND".equals(claim.getClaimType())){
                    claim.setResolvedAt(LocalDateTime.now());
                }
            }
            case "REJECT", "REJECTED" -> {
                claim.setStatus("REJECTED");
                claim.setRefundAmount(null);
                claim.setResolvedAt(LocalDateTime.now());

                inventoryService.adjustStock(
                        claim.getProductId(), claim.getQuantity(),
                        "SUPPLIER_CLAIM_RESTORE", "supplier_claims", claim.getId(),
                        "Supplier ปฏิเสธการเคลม # " + claim.getId() + "คืนสต็อก", supplier
                );
            }
            default -> throw new BadRequestException("action ต้องเป็น APPROVE หรือ REJECT");
        }
        return toResponse(supplierClaimRepository.save(claim));
    }

    private  SupplierClaim requiredClaim(Long id) {
        return supplierClaimRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("ไม่พบหมายเลขเตลมเลข: " + id));
    }

    private SupplierClaim requiredOwnedClaim(Long id, User supplier) {
        SupplierClaim claim = requiredClaim(id);
        if (claim.getSupplierUser() == null || !claim.getSupplierUser().getId().equals(supplier.getId()) ) {
            throw new BadRequestException("คุณไม่มีสิทธิ์เข้าถึงการเคลมนี้");
        }
        return claim;
    }

    private SupplierClaimResponse toResponse(SupplierClaim claim) {
        List<String> files = supplierClaimAttachmentRepository.findBySupplierClaim_Id(claim.getId())
                .stream().map(SupplierClaimAttachment::getFileUrl).toList();

        var po = claim.getPurchaseOrder();
        var supplierUser = claim.getSupplierUser();
        var adminUser = claim.getAdminUser();

        return new SupplierClaimResponse(
                claim.getId(),
                po != null ? po.getId() : null,
                po != null ? po.getPoNumber() : null,
                claim.getProductId(),
                claim.getProductName(),
                claim.getQuantity(),
                claim.getClaimType(),
                claim.getStatus(),
                claim.getDescription(),
                claim.getSupplierResponse(),
                claim.getRefundAmount(),
                supplierUser != null ? new SupplierClaimResponse.SupplierInfo(supplierUser.getId(), supplierUser.getEmail(), supplierUser.getFullName()) : null,
                adminUser != null ? new SupplierClaimResponse.AdminInfo(adminUser.getId(), adminUser.getEmail(), adminUser.getFullName()) : null,
                files,
                claim.getResolvedAt(),
                claim.getCreatedAt(),
                claim.getUpdatedAt()
        );
    }

}
