package com.ctsousa.mover.resource;

import com.ctsousa.mover.core.api.TransactionBalanceApi;
import com.ctsousa.mover.domain.Transaction;
import com.ctsousa.mover.response.TransactionBalanceResponse;
import com.ctsousa.mover.service.TransactionBalanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/transactions-balance")
public class TransactionBalanceResource implements TransactionBalanceApi {

    private final TransactionBalanceService transactionBalanceService;

    public TransactionBalanceResource(TransactionBalanceService transactionBalanceService) {
        this.transactionBalanceService = transactionBalanceService;
    }

    @Override
    public ResponseEntity<List<TransactionBalanceResponse>> balances(String filterURI) {
        var filter = new Transaction.Filter(filterURI);
        var response = transactionBalanceService.calculateBalances(filter.getAccountsId(), filter.getDtInitial(), filter.getDtFinal());
        return ResponseEntity.ok(response);
    }
}
