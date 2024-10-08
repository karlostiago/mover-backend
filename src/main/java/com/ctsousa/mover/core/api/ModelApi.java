package com.ctsousa.mover.core.api;

import com.ctsousa.mover.response.ModelResponse;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

public interface ModelApi {

    @GetMapping(value = "/filterBy")
    ResponseEntity<List<ModelResponse>> filterBy(@Param("search") String search);

    @GetMapping(value = "/find-by-brand-id")
    ResponseEntity<List<ModelResponse>> findByBrandId(@Param("branId") Long brandId);
}
