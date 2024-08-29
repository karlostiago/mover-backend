package com.ctsousa.mover.core.api;

import com.ctsousa.mover.response.BankIconResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

public interface AccountApi {

    @GetMapping("/icons")
    ResponseEntity<List<BankIconResponse>> findAllIcons();
}
