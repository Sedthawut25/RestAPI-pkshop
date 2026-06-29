package com.pkshop.api.customs;

import com.pkshop.common.response.ApiResponse;
import com.pkshop.domain.importflow.entity.ImportDocument;
import com.pkshop.domain.importflow.entity.ImportLotItem;
import com.pkshop.domain.importflow.repository.ImportDocumentRepository;
import com.pkshop.domain.importflow.repository.ImportLotItemRepository;
import com.pkshop.domain.user.entity.User;
import com.pkshop.domain.user.repository.UserRepository;
import com.pkshop.dto.customs.CustomsDecisionRequest;
import com.pkshop.dto.customs.CustomsImportDocumentDetailResponse;
import com.pkshop.service.importflow.ImportFlowService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/customs/import-documents")
public class CustomsImportDocumentController {

    private final ImportDocumentRepository docRepo;
    private final ImportFlowService importFlowService;
    private final UserRepository userRepo;
    private final ImportLotItemRepository lotItemRepo;

    public CustomsImportDocumentController(
            ImportDocumentRepository docRepo,
            ImportFlowService importFlowService,
            UserRepository userRepo,
            ImportLotItemRepository lotItemRepo
    ) {
        this.docRepo = docRepo;
        this.importFlowService = importFlowService;
        this.userRepo = userRepo;
        this.lotItemRepo = lotItemRepo;
    }

    private User currentUser() {
        return (User)  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @GetMapping
    public ApiResponse<List<ImportDocument>> list(@RequestParam(defaultValue = "UNDER_REVIEW") String status) {
        return ApiResponse.ok("Documents", docRepo.findByStatus(status));
    }

    @PostMapping("/{docId}/approve")
    public ApiResponse<Void> approve(@PathVariable Long docId, @Valid @RequestBody CustomsDecisionRequest req) {
        importFlowService.customsApprove(docId, req.comment(), currentUser());
        return ApiResponse.ok("Approved", null);
    }

    @PostMapping("/{docId}/reject")
    public ApiResponse<Void> reject(@PathVariable Long docId, @Valid @RequestBody CustomsDecisionRequest req) {
        importFlowService.customsReject(docId, req.comment(), currentUser());
        return ApiResponse.ok("Rejected", null);
    }

    @GetMapping("/{docId}")
    public ApiResponse<ImportDocument> getOne(@PathVariable Long docId) {
        return ApiResponse.ok("Document", docRepo.findById(docId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found")));
    }

    @GetMapping("/{docId}/detail")
    public ApiResponse<CustomsImportDocumentDetailResponse> detail(@PathVariable Long docId) {

        ImportDocument doc = docRepo.findByIdWithLotAndSubmitter(docId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found"));

        var lot = doc.getImportLot();

        List<ImportLotItem> items = lotItemRepo.findByImportLotId(lot.getId());

        var mappedItems = items.stream().map(it ->
                new CustomsImportDocumentDetailResponse.Item(
                        it.getProduct().getId(),
                        it.getProduct().getName(),
                        it.getQty(),
                        it.getUnitCost(),
                        it.getLineCost()
                )
        ).toList();

        var lotInfo = new CustomsImportDocumentDetailResponse.LotInfo(
                lot.getId(),
                lot.getLotNumber(),
                lot.getShippingMethod(),
                lot.getOriginCountry(),
                lot.getTotalImportCost()
        );

        var submitter = doc.getSubmittedBy();
        var submittedByInfo = new CustomsImportDocumentDetailResponse.UserInfo(
                submitter.getId(),
                submitter.getEmail(),
                submitter.getFullName()
        );

        var res = new CustomsImportDocumentDetailResponse(
                doc.getId(),
                doc.getDocNumber(),
                doc.getDocType(),
                doc.getStatus(),
                doc.getSubmittedAt(),
                doc.getComment(),
                lotInfo,
                submittedByInfo,
                mappedItems
        );

        return ApiResponse.ok("Document detail", res);
    }
}