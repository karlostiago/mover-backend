package com.ctsousa.mover.core.api;

import com.ctsousa.mover.response.AccountResponse;
import com.ctsousa.mover.response.BankIconResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface AccountApi {

    @GetMapping("/icons")
    ResponseEntity<List<BankIconResponse>> findAllIcons();

    @GetMapping("/filterBy")
    ResponseEntity<List<AccountResponse>> filterBy(@RequestParam("search") String search);
}
