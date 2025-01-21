package com.ctsousa.mover.core.api;

import com.ctsousa.mover.response.BalanceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface BalanceApi {

    @GetMapping
    ResponseEntity<BalanceResponse> balances(@RequestParam("filterURI") String filterURI);
}
