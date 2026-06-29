package com.pkshop.service.inventory;

import com.pkshop.domain.catalog.entity.Product;
import com.pkshop.domain.catalog.repository.ProductRepository;
import com.pkshop.domain.inventory.entity.InventoryTransaction;
import com.pkshop.domain.inventory.repository.InventoryTransactionRepository;
import com.pkshop.domain.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;

@Service
public class InventoryService {

    private final ProductRepository productRepo;
    private final InventoryTransactionRepository txnRepo;

    public InventoryService(ProductRepository productRepo, InventoryTransactionRepository txnRepo) {
        this.productRepo = productRepo;
        this.txnRepo = txnRepo;
    }

    @Transactional
    public void addStockFromImport(Long productId, int qty, BigDecimal unitCost, Long importLotId, User actor) {
        Product product = productRepo.findById(productId).orElseThrow();

        // 1) ledger
        InventoryTransaction txn = new InventoryTransaction();
        txn.setProduct(product);
        txn.setTxnType("IMPORT_IN");
        txn.setRefTable("import_lots");
        txn.setRefId(importLotId);
        txn.setQtyChange(qty);
        txn.setUnitCost(unitCost);
        txn.setNote("Receive import lot");
        txn.setCreatedBy(actor);
        txn.setCreatedAt(Instant.now());
        txnRepo.save(txn);

        product.setStockQty(product.getStockQty() + qty);


        int newStock = product.getStockQty();
        BigDecimal oldAvg = product.getImportCostAvg();
        BigDecimal numerator = oldAvg.multiply(BigDecimal.valueOf(newStock - qty))
                .add(unitCost.multiply(BigDecimal.valueOf(qty)));
        BigDecimal newAvg = numerator.divide(BigDecimal.valueOf(newStock), 2, java.math.RoundingMode.HALF_UP);
        product.setImportCostAvg(newAvg);

        productRepo.save(product);
    }

    @Transactional
    public void deductStockForSale(Long productId, int qty, Long orderId, User actor) {
        if (qty <= 0) return;

        Product product = productRepo.findById(productId).orElseThrow();

        if (product.getStockQty() < qty) {
            throw new com.pkshop.common.exception.BadRequestException("Insufficient stock for productId=" + productId);
        }

        InventoryTransaction txn = new InventoryTransaction();
        txn.setProduct(product);
        txn.setTxnType("SALE_OUT");
        txn.setRefTable("orders");
        txn.setRefId(orderId);
        txn.setQtyChange(-qty);
        txn.setUnitCost(null);
        txn.setNote("Sale out");
        txn.setCreatedBy(actor);
        txn.setCreatedAt(Instant.now());
        txnRepo.save(txn);

        product.setStockQty(product.getStockQty() - qty);
        productRepo.save(product);
    }

}
