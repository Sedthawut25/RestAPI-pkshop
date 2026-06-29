package com.pkshop.mapper.b2b;

import com.pkshop.domain.b2b.entity.SupplierQuotation;
import com.pkshop.dto.admin.b2b.SupplierQuotationDto;

import java.util.List;

public class SupplierQuotationMapper {

    public static SupplierQuotationDto toDto(SupplierQuotation q) {
        List<SupplierQuotationDto.ItemDto> items = (q.getItems() == null) ? List.of()
                : q.getItems().stream().map(it ->
                new SupplierQuotationDto.ItemDto(
                        it.getId(),
                        it.getProduct() != null ? it.getProduct().getId() : null,
                        it.getProduct() != null ? it.getProduct().getName() : null,
                        it.getQty(),
                        it.getQuotedUnitCost(),
                        it.getLeadTimeDays()
                )).toList();

        return new SupplierQuotationDto(
                q.getId(),
                q.getStatus(),
                q.getQuotationNumber(),
                q.getCreatedAt(),
                q.getValidUntil(),
                q.getPurchaseOrder() != null ? q.getPurchaseOrder().getId() : null,
                q.getSupplierUser() != null ? q.getSupplierUser().getId() : null,
                items
        );
    }
}