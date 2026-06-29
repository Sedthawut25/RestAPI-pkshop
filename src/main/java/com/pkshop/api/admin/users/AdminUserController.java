package com.pkshop.api.admin.users;

import com.pkshop.common.response.ApiResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final JdbcTemplate jdbc;

    public AdminUserController(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @GetMapping
    public ApiResponse<List<UserOptionResponse>> list(@RequestParam(required = false) String role) {

        // ถ้า role=SUPPLIER -> คืนเฉพาะ supplier
        if (role != null && role.equalsIgnoreCase("SUPPLIER")) {
            String sql = """
                SELECT u.id, u.email, u.full_name
                FROM users u
                JOIN user_roles ur ON ur.user_id = u.id
                JOIN roles r ON r.id = ur.role_id
                WHERE r.name = 'SUPPLIER'
                ORDER BY u.full_name ASC
            """;

            List<UserOptionResponse> rows = jdbc.query(sql, (rs, i) ->
                    new UserOptionResponse(
                            rs.getLong("id"),
                            rs.getString("email"),
                            rs.getString("full_name"),
                            rs.getString("full_name") // label
                    )
            );

            return ApiResponse.ok("Users", rows);
        }

        // ถ้าไม่ส่ง role มา/role อื่น -> คืน array ว่างก่อน (กัน UI พัง)
        return ApiResponse.ok("Users", List.of());
    }

    public record UserOptionResponse(
            Long id,
            String email,
            String fullName,
            String label
    ) {}
}