package com.pkshop.dto.customs;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record CustomsDocumentDetailResponse(
        Long id,
        String docNumber,
        String docType,
        String status,
        Instant submittedAt,
        LotInfo importLot,
        UserInfo submittedBy,
        List<Item> items
) {
    public record LotInfo(Long id, String lotNumber) {}
    public record UserInfo(Long id, String email) {}

    public record Item(
            Long productId,
            String productName,
            Integer qty,
            BigDecimal unitCost,
            BigDecimal lineCost
    ) {}
}