package com.ctsousa.mover.core.api;

import com.ctsousa.mover.response.BankIconResponse;
import com.ctsousa.mover.response.CardResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface CardApi {

    @GetMapping("/icons")
    ResponseEntity<List<BankIconResponse>> findAllIcons();

    @GetMapping("/filterBy")
    ResponseEntity<List<CardResponse>> filterBy(@RequestParam("search") String search);
}
