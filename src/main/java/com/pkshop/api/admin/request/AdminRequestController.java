package com.pkshop.api.admin.request;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pkshop.domain.sales.entity.OutOfStockRequest;
import com.pkshop.domain.sales.repository.OutOfStockRequestRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/requests")
@RequiredArgsConstructor
public class AdminRequestController {

    private final OutOfStockRequestRepository outOfStockRequestRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OutOfStockRequest>> getAllRequests() {
        List<OutOfStockRequest> outOfStockRequests = outOfStockRequestRepository.findAll();
        return ResponseEntity.ok(outOfStockRequests);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateRequestStatus(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String newStatus = payload.get("status");

        OutOfStockRequest request = outOfStockRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ไม่พบคำขอนี้"));

        request.setStatus(newStatus);
        outOfStockRequestRepository.save(request);

        return ResponseEntity.ok(Map.of("message", "อัปเดตสถานะสำเร็จ", "status", newStatus));
    }
}
