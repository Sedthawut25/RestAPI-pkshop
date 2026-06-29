package com.pkshop.api.admin.dashboard;

import com.pkshop.common.response.ApiResponse;
import com.pkshop.dto.admin.dashboard.*;
import com.pkshop.service.dashboard.DashboardService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    private LocalDate parseDate(String date) {

        if (date == null || date.isBlank()) {
            return null;
        }

        return LocalDate.parse(date);
    }

    @GetMapping("/summary")
    public ApiResponse<DashboardSummaryResponse> summary(
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to,
            @RequestParam(defaultValue = "10") int lowStockThreshold
    ) {

        return ApiResponse.ok(
                "Summary",
                dashboardService.summary(
                        parseDate(from),
                        parseDate(to),
                        lowStockThreshold
                )
        );
    }

    @GetMapping("/sales-series")
    public ApiResponse<SalesSeriesResponse> salesSeries(
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to,
            @RequestParam(defaultValue = "DAILY") String granularity
    ) {

        return ApiResponse.ok(
                "Sales series",
                dashboardService.salesSeries(
                        parseDate(from),
                        parseDate(to),
                        granularity
                )
        );
    }

    @GetMapping("/best-sellers")
    public ApiResponse<List<BestSellerResponse>> bestSellers(
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to,
            @RequestParam(defaultValue = "10") int limit
    ) {

        return ApiResponse.ok(
                "Best sellers",
                dashboardService.bestSellers(
                        parseDate(from),
                        parseDate(to),
                        limit
                )
        );
    }

    @GetMapping("/dead-stock")
    public ApiResponse<List<DeadStockResponse>> deadStock(
            @RequestParam(defaultValue = "60") int days,
            @RequestParam(defaultValue = "20") int limit
    ) {

        return ApiResponse.ok(
                "Dead stock",
                dashboardService.deadStock(days, limit)
        );
    }
}