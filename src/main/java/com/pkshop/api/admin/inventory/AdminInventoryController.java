package com.pkshop.api.admin.inventory;

import com.pkshop.common.response.ApiResponse;
import com.pkshop.domain.inventory.entity.InventoryTransaction;
import com.pkshop.domain.inventory.repository.InventoryTransactionRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/inventory")
public class AdminInventoryController {

    private final InventoryTransactionRepository txnRepo;

    public AdminInventoryController(InventoryTransactionRepository txnRepo) {
        this.txnRepo = txnRepo;
    }

    @GetMapping("/transactions")
    public ApiResponse<List<InventoryTransaction>> all() {
        return ApiResponse.ok("Inventory transactions", txnRepo.findAll());
    }
}
