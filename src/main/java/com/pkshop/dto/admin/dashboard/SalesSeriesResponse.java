package com.pkshop.dto.admin.dashboard;

import java.math.BigDecimal;
import java.util.List;

public record SalesSeriesResponse(
        String granularity, // DAILY or MONTHLY
        List<Point> points
) {
    public record Point(String label, BigDecimal revenue, long orders) {}
}