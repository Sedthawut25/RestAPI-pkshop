/*package com.pkshop.domain.b2b;

import com.pkshop.dto.admin.b2b.SupplierOptionResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminSupplierService {

    private final JdbcTemplate jdbc;

    public AdminSupplierService(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<SupplierOptionResponse> listSuppliers() {
        String sql = """
            SELECT u.id, u.email, u.full_name
            FROM users u
            JOIN user_roles ur ON ur.user_id = u.id
            JOIN roles r ON r.id = ur.role_id
            WHERE r.name = 'SUPPLIER'
            ORDER BY u.id DESC
        """;

        return jdbc.query(sql, (rs, i) ->
                new SupplierOptionResponse(
                        rs.getLong("id"),
                        rs.getString("email"),
                        rs.getString("full_name")
                )
        );
    }
}*/