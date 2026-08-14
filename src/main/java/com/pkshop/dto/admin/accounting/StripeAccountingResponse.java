package com.pkshop.dto.admin.accounting;

import java.math.BigDecimal;
import java.util.List;

public record StripeAccountingResponse(
        BalanceInfo balance,
        List<TransactionInfo> recentTransactions
) {
    public  record BalanceInfo(
            BigDecimal available,
            BigDecimal pending
    ){}

    public record TransactionInfo(
            String id,
            Long createdTimestamp,
            BigDecimal amount,
            BigDecimal fee,
            BigDecimal net,
            String type,
            String description
    ){}
}
