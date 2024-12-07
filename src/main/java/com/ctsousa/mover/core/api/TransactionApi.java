package com.ctsousa.mover.core.api;

import com.ctsousa.mover.response.BalanceResponse;
import com.ctsousa.mover.response.TransactionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface TransactionApi {

    @GetMapping("/filterBy")
    ResponseEntity<List<TransactionResponse>> filterBy(@RequestParam("search") String search);

    @PutMapping("/{id}/pay")
    ResponseEntity<TransactionResponse> pay(@PathVariable Long id);

    @PutMapping("/{id}/refund")
    ResponseEntity<TransactionResponse> refund(@PathVariable Long id);

    @GetMapping("/balances")
    ResponseEntity<BalanceResponse> balance();
}
