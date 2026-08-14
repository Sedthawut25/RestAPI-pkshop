package com.pkshop.service.accounting;

import com.pkshop.dto.admin.accounting.StripeAccountingResponse;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Balance;
import com.stripe.model.BalanceTransaction;
import com.stripe.model.BalanceTransactionCollection;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class AccountingService {

    @Value("${stripe.secretKey}")
    private String stripeSecretKey;

    @PostConstruct
    public void init(){
        Stripe.apiKey = stripeSecretKey;
    }

    public StripeAccountingResponse getStripeSummary() throws StripeException {
        Balance balance = Balance.retrieve();
        BigDecimal available = BigDecimal.ZERO;
        BigDecimal pending = BigDecimal.ZERO;

        // คืนค่าเป็น List แยกสกุลเงิน
        if(balance.getAvailable() != null && !balance.getAvailable().isEmpty()) {
            available = BigDecimal.valueOf(balance.getAvailable().get(0).getAmount())
                    .divide(BigDecimal.valueOf(100),2, RoundingMode.HALF_UP);
        }

        if(balance.getPending() != null && !balance.getPending().isEmpty()) {
            pending = BigDecimal.valueOf(balance.getPending().get(0).getAmount())
                    .divide(BigDecimal.valueOf(100),2, RoundingMode.HALF_UP);
        }

        // ดึงประวัติรายการล่าสุด เพื่อดึงมาแสดงบนหน้าจอ
        Map<String, Object> params = new HashMap<>();
        params.put("limit", 100);
        BalanceTransactionCollection transaction = BalanceTransaction.list(params);

        // Map ข้อมูล ใน Transaction ของ stripe มาใส่ใน DTO เพิ่อจะนำมาแสดงบนจอ
        List<StripeAccountingResponse.TransactionInfo> txnList = transaction.getData().stream().map(tx ->  {
            BigDecimal amount = BigDecimal.valueOf(tx.getAmount()).divide(BigDecimal.valueOf(100),2, RoundingMode.HALF_UP);
            BigDecimal fee = BigDecimal.valueOf(tx.getFee()).divide(BigDecimal.valueOf(100),2, RoundingMode.HALF_UP);
            BigDecimal net = BigDecimal.valueOf(tx.getNet()).divide(BigDecimal.valueOf(100),2, RoundingMode.HALF_UP);

            return new StripeAccountingResponse.TransactionInfo(
                    tx.getId(),
                    tx.getCreated(),
                    amount,
                    fee,
                    net,
                    tx.getType(),
                    tx.getDescription()
            );
        }).toList();

        return new StripeAccountingResponse(
                new StripeAccountingResponse.BalanceInfo(available, pending),
                txnList
        );
    }
}
