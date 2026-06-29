package com.pkshop.service.dashboard;

import com.pkshop.domain.sales.repository.DashboardRepository;
import com.pkshop.dto.admin.dashboard.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class DashboardService {

    private final DashboardRepository repo;

    public DashboardService(DashboardRepository repo) {
        this.repo = repo;
    }

    public DashboardSummaryResponse summary(LocalDate from, LocalDate to, int lowStockThreshold) {

        // ป้องกัน null
        if (from == null) {
            from = LocalDate.now().minusDays(30);
        }

        if (to == null) {
            to = LocalDate.now();
        }

        long orders = repo.countPaidOrders(from, to);

        BigDecimal revenue = repo.sumRevenue(from, to);
        if (revenue == null) {
            revenue = BigDecimal.ZERO;
        }

        BigDecimal cogs = repo.sumCogs(from, to);
        if (cogs == null) {
            cogs = BigDecimal.ZERO;
        }

        BigDecimal gross = revenue.subtract(cogs);

        long lowStockCount = repo.countLowStock(lowStockThreshold);
        long pendingShip = repo.countPendingShipment();

        BigDecimal importExpense = repo.sumImportExpense(from, to);
        if (importExpense == null) {
            importExpense = BigDecimal.ZERO;
        }

        long docsUnderReview = repo.countCustomsDocsUnderReview();
        long docsRejected = repo.countCustomsDocsRejected(from, to);
        long lotsReady = repo.countLotsReadyToReceive();

        return new DashboardSummaryResponse(
                orders,
                revenue,
                importExpense,
                cogs,
                gross,
                lowStockCount,
                pendingShip,
                docsUnderReview,
                docsRejected,
                lotsReady
        );
    }

    public SalesSeriesResponse salesSeries(LocalDate from, LocalDate to, String granularity) {

        // ป้องกัน null
        if (from == null) {
            from = LocalDate.now().minusDays(30);
        }

        if (to == null) {
            to = LocalDate.now();
        }

        String g = (granularity == null || granularity.isBlank())
                ? "DAILY"
                : granularity.trim().toUpperCase();

        if ("MONTHLY".equals(g)) {

            var rows = repo.salesMonthly(from, to);

            List<SalesSeriesResponse.Point> points = rows.stream().map(r ->
                    new SalesSeriesResponse.Point(
                            (String) r.get("m"),
                            (BigDecimal) r.get("revenue"),
                            ((Number) r.get("orders")).longValue()
                    )
            ).toList();

            return new SalesSeriesResponse("MONTHLY", points);
        }

        var rows = repo.salesDaily(from, to);

        List<SalesSeriesResponse.Point> points = rows.stream().map(r ->
                new SalesSeriesResponse.Point(
                        String.valueOf(r.get("d")),
                        (BigDecimal) r.get("revenue"),
                        ((Number) r.get("orders")).longValue()
                )
        ).toList();

        return new SalesSeriesResponse("DAILY", points);
    }

    public List<BestSellerResponse> bestSellers(LocalDate from, LocalDate to, int limit) {

        // ป้องกัน null
        if (from == null) {
            from = LocalDate.now().minusDays(30);
        }

        if (to == null) {
            to = LocalDate.now();
        }

        return repo.bestSellers(from, to, limit).stream().map(r ->
                new BestSellerResponse(
                        ((Number) r.get("product_id")).longValue(),
                        (String) r.get("product_name"),
                        ((Number) r.get("qty_sold")).longValue(),
                        (BigDecimal) r.get("revenue")
                )
        ).toList();
    }

    public List<DeadStockResponse> deadStock(int days, int limit) {
            return repo.deadStock(days, limit).stream().map(r -> {
                Number daySinceObj = (Number) r.get("day_since_last_stocked");
                int daySince = (daySinceObj != null)  ? daySinceObj.intValue() : -1;

                return new DeadStockResponse(
                        ((Number) r.get("product_id")).longValue(),
                        ((String) r.get("product_name")),
                        ((Number) r.get("stock_qty")).intValue(),
                        daySince
                );
            }).toList();
    }
}