package com.ctsousa.mover.core.api;

import com.ctsousa.mover.response.TransactionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface InvoiceApi {

    @GetMapping("/invoice/{id}")
    ResponseEntity<List<TransactionResponse>> searchById(@PathVariable Long id);
}
