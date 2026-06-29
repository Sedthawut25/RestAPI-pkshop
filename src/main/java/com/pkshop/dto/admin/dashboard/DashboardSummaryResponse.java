package com.pkshop.dto.admin.dashboard;

import java.math.BigDecimal;

public record DashboardSummaryResponse(
        long ordersPaidCount,
        BigDecimal revenueTotal,
        BigDecimal importExpenseTotal,
        BigDecimal cogsTotal,
        BigDecimal grossProfit,
        long productsLowStockCount,
        long ordersPendingShipmentCount,
        long customsDocsUnderReviewCount,
        long customsDocsRejectedCount,
        long lotsReadyToReceiveCount
) {}