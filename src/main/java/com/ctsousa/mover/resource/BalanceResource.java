package com.ctsousa.mover.resource;

import com.ctsousa.mover.core.api.BalanceApi;
import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.domain.Transaction;
import com.ctsousa.mover.response.BalanceResponse;
import com.ctsousa.mover.service.BalanceService;
import com.ctsousa.mover.service.TransactionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/balances")
public class BalanceResource implements BalanceApi {

    private final TransactionService transactionService;

    private final BalanceService balanceService;

    public BalanceResource(TransactionService transactionService, BalanceService balanceService) {
        this.transactionService = transactionService;
        this.balanceService = balanceService;
    }

    @Override
    public ResponseEntity<BalanceResponse> balances(String filterURI) {
        var filter = new Transaction.Filter(filterURI);

        Page<TransactionEntity> page = transactionService.search(filter.getDtInitial(), filter.getDtFinal(), filter.getAccountsId(),
                filter.getText(), PageRequest.of(0, Integer.MAX_VALUE));

        BalanceResponse response = balanceService.calculateBalances(filter.getAccountsId(), page.stream().toList());

        return ResponseEntity.ok(response);
    }
}
