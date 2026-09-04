package com.pkshop.api.admin.importflow;

import com.pkshop.common.response.ApiResponse;
import com.pkshop.domain.importflow.entity.ImportDocument;
import com.pkshop.domain.importflow.entity.ImportLot;
import com.pkshop.domain.importflow.entity.ImportLotItem;
import com.pkshop.domain.importflow.repository.ImportDocumentRepository;
import com.pkshop.domain.importflow.repository.ImportLotItemRepository;
import com.pkshop.domain.importflow.repository.ImportLotRepository;
import com.pkshop.dto.admin.importflow.*;
import com.pkshop.domain.user.entity.User;
import com.pkshop.domain.user.repository.UserRepository;
import com.pkshop.service.importflow.ImportFlowService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/import")
public class AdminImportController {

    private final ImportFlowService importFlowService;
    private final UserRepository userRepo;
    private final ImportLotRepository lotRepo;
    private final ImportLotItemRepository lotItemRepo;
    private final ImportDocumentRepository docRepo;

    public AdminImportController(ImportFlowService importFlowService, UserRepository userRepo,ImportLotRepository lotRepo,
                                 ImportLotItemRepository lotItemRepo,
                                 ImportDocumentRepository docRepo) {
        this.importFlowService = importFlowService;
        this.userRepo = userRepo;
        this.lotRepo = lotRepo;
        this.lotItemRepo = lotItemRepo;
        this.docRepo = docRepo;
    }

    private User currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) {
            throw new RuntimeException("Unauthenticated user");
        }

        Object principal = auth.getPrincipal();

        if (principal instanceof com.pkshop.domain.user.entity.User user) {
            return user;
        }

        if (principal instanceof org.springframework.security.core.userdetails.UserDetails userDetails) {
            String username = userDetails.getUsername();
            return userRepo.findByEmail(username)
                    .orElseGet(() -> {
                        try {
                            return userRepo.findById(Long.parseLong(username))
                                    .orElseThrow(() -> new RuntimeException("User not found: " + username));
                        } catch (NumberFormatException e) {
                            throw new RuntimeException("User not found with Email: " + username);
                        }
                    });
        }

        if (principal instanceof String identifier) {
            return userRepo.findByEmail(identifier)
                    .orElseGet(() -> {
                        try {
                            Long userId = Long.parseLong(identifier);
                            return userRepo.findById(userId)
                                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
                        } catch (NumberFormatException e) {
                            throw new RuntimeException("User not found with Email: " + identifier);
                        }
                    });
        }

        throw new RuntimeException("Unsupported principal type: " + principal.getClass().getName());
    }


    @PostMapping("/lots")
    public ApiResponse<ImportLot> createLot(@Valid @RequestBody CreateImportLotRequest req) {
        return ApiResponse.ok("Created import lot", importFlowService.createImportLot(req, currentUser()));
    }

    @PostMapping("/lots/{lotId}/items")
    public ApiResponse<ImportLotItem> addLotItem(@PathVariable Long lotId, @Valid @RequestBody AddImportLotItemRequest req) {
        return ApiResponse.ok("Added lot item", importFlowService.addLotItem(lotId, req));
    }

    @PostMapping("/lots/{lotId}/documents")
    public ApiResponse<ImportDocument> createDoc(@PathVariable Long lotId, @Valid @RequestBody CreateImportDocumentRequest req) {
        return ApiResponse.ok("Created import document", importFlowService.createImportDocument(lotId, req, currentUser()));
    }

    @PostMapping("/documents/{docId}/submit")
    public ApiResponse<ImportDocument> submitToCustoms(@PathVariable Long docId) {
        return ApiResponse.ok("Submitted to customs", importFlowService.submitToCustoms(docId));
    }

    @PostMapping("/lots/{lotId}/receive")
    public ApiResponse<Void> receive(@PathVariable Long lotId, @Valid @RequestBody ReceiveImportLotRequest req) {
        importFlowService.receiveImportLot(lotId, req.arrivalDate(), currentUser());
        return ApiResponse.ok("Stock received", null);
    }

    @GetMapping("/lots")
    public ApiResponse<List<ImportLot>> listLots(@RequestParam(required = false) String status) {
        if (status == null || status.isBlank()) {
            return ApiResponse.ok("Lots", lotRepo.findAll(Sort.by(Sort.Direction.DESC, "id")));
        }
        // ถ้าอยาก filter status จริง ๆ ให้เพิ่ม method ใน repo: findByStatus(String status)
        // ตอนนี้ใช้ filter แบบง่ายใน memory ก่อน (โปรเจกต์จบโอเค)
        List<ImportLot> all = lotRepo.findAll(Sort.by(Sort.Direction.DESC, "id"));
        String s = status.trim().toUpperCase();
        return ApiResponse.ok("Lots", all.stream().filter(x -> s.equalsIgnoreCase(x.getStatus())).toList());
    }

    // ✅ 2) Lot detail: lot + items + documents
    @GetMapping("/lots/{lotId}")
    public ApiResponse<Map<String, Object>> lotDetail(@PathVariable Long lotId) {
        ImportLot lot = lotRepo.findById(lotId).orElseThrow();
        List<ImportLotItem> items = lotItemRepo.findByImportLotId(lotId);
        List<ImportDocument> docs = docRepo.findByImportLotId(lotId);

        Map<String, Object> data = new HashMap<>();
        data.put("lot", lot);
        data.put("items", items);
        data.put("documents", docs);

        return ApiResponse.ok("Lot detail", data);
    }
}
