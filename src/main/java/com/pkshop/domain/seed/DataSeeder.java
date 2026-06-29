package com.pkshop.domain.seed;

import com.pkshop.domain.user.entity.Role;
import com.pkshop.domain.user.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepo;

    public DataSeeder(RoleRepository roleRepo) {
        this.roleRepo = roleRepo;
    }

    @Override
    public void run(String... args) {
        List<String> roles = List.of("ADMIN", "CUSTOMER", "SUPPLIER", "CUSTOMS");
        for (String r : roles) {
            roleRepo.findByName(r).orElseGet(() -> {
                Role role = new Role();
                role.setName(r);
                return roleRepo.save(role);
            });
        }
    }
}
