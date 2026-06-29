package com.pkshop.dto.customer.shop;

import java.util.List;

public record ShopFiltersResponse(
        List<IdName> categories,
        List<IdName> brands,
        List<ModelDto> models
) {
    public record IdName(Long id, String name) {}
    public record ModelDto(Long id, String name, Long brandId) {}
}