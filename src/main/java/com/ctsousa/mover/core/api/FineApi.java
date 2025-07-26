package com.ctsousa.mover.core.api;

import com.ctsousa.mover.response.FineResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

public interface FineApi {

    @PutMapping("/{id}/synchronize")
    ResponseEntity<FineResponse> synchronize(@PathVariable Long id);
}
