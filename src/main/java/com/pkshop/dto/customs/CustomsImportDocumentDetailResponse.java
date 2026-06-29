package com.pkshop.dto.customs;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record CustomsImportDocumentDetailResponse(
        Long id,
        String docNumber,
        String docType,
        String status,
        Instant submittedAt,
        String comment,

        LotInfo lot,
        UserInfo submittedBy,

        List<Item> items
) {
    public record LotInfo(
            Long id,
            String lotNumber,
            String shippingMethod,
            String originCountry,
            BigDecimal totalImportCost
    ) {}

    public record UserInfo(
            Long id,
            String email,
            String fullName
    ) {}

    public record Item(
            Long productId,
            String productName,
            Integer qty,
            BigDecimal unitCost,
            BigDecimal lineCost
    ) {}
}