package com.ctsousa.mover.core.api;

import com.ctsousa.mover.response.BalanceResponse;
import com.ctsousa.mover.response.TransactionResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

public interface TransactionApi {

    @GetMapping("/filterBy")
    ResponseEntity<List<TransactionResponse>> filterBy(@RequestParam("search") String uri);

    @PutMapping("/{id}/pay/{paymentDate}")
    ResponseEntity<TransactionResponse> pay(@PathVariable Long id, @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate paymentDate);

    @PutMapping("/{id}/refund")
    ResponseEntity<TransactionResponse> refund(@PathVariable Long id);

    @GetMapping("/balances")
    ResponseEntity<BalanceResponse> balance();

    @DeleteMapping("/remove/{id}")
    void delete(@PathVariable Long id, @RequestParam("deleteOnlyThis") Boolean deleteOnlyThis);
}
