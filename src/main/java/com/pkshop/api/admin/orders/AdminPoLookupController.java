package com.pkshop.api.admin.orders;

import com.pkshop.common.response.ApiResponse;
import com.pkshop.dto.admin.orders.PoProductOption;
import com.pkshop.dto.admin.orders.SupplierOption;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/po")
public class AdminPoLookupController {

    private final JdbcTemplate jdbc;

    public AdminPoLookupController(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    // ✅ สำหรับ dropdown supplier ในหน้า /admin/po/new
    @GetMapping("/suppliers")
    public ApiResponse<List<SupplierOption>> suppliers() {
        String sql = """
            SELECT u.id, u.email, u.full_name
            FROM users u
            JOIN user_roles ur ON ur.user_id = u.id
            JOIN roles r ON r.id = ur.role_id
            WHERE r.name = 'SUPPLIER'
            ORDER BY u.full_name ASC
        """;

        List<SupplierOption> rows = jdbc.query(sql, (rs, i) -> {
            long id = rs.getLong("id");
            String email = rs.getString("email");
            String fullName = rs.getString("full_name");
            return new SupplierOption(id, fullName, email, fullName); // label = fullName
        });

        return ApiResponse.ok("Suppliers", rows);
    }

    // ✅ ค้นหาสินค้าที่จะใส่ PO: เฉพาะ stock_qty = 0
    @GetMapping("/products")
    public ApiResponse<List<PoProductOption>> products(
            @RequestParam(defaultValue = "") String q,
            @RequestParam(defaultValue = "30") int limit
    ) {
        // กัน limit เกิน
        int lim = Math.min(Math.max(limit, 1), 100);

        String sql = """
            SELECT id, sku, name, import_cost_avg, stock_qty
            FROM products
            WHERE stock_qty = 0
              AND ( ? = '' OR LOWER(name) LIKE CONCAT('%', LOWER(?), '%')
                        OR LOWER(sku)  LIKE CONCAT('%', LOWER(?), '%') )
            ORDER BY id DESC
            LIMIT ?
        """;

        List<PoProductOption> rows = jdbc.query(
                sql,
                ps -> {
                    ps.setString(1, q == null ? "" : q.trim());
                    ps.setString(2, q == null ? "" : q.trim());
                    ps.setString(3, q == null ? "" : q.trim());
                    ps.setInt(4, lim);
                },
                (rs, i) -> new PoProductOption(
                        rs.getLong("id"),
                        rs.getString("sku"),
                        rs.getString("name"),
                        rs.getBigDecimal("import_cost_avg"),
                        rs.getInt("stock_qty")
                )
        );

        return ApiResponse.ok("Products", rows);
    }
}