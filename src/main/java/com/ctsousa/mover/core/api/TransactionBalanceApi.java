package com.ctsousa.mover.core.api;

import com.ctsousa.mover.response.TransactionBalanceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface TransactionBalanceApi {

    @GetMapping
    ResponseEntity<List<TransactionBalanceResponse>> balances(@RequestParam("filterURI") String filterURI);
}
