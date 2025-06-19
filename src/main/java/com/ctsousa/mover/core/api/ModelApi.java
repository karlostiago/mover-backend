package com.ctsousa.mover.core.api;

import com.ctsousa.mover.response.ModelResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ModelApi {

    @GetMapping(value = "/filterBy")
    ResponseEntity<Page<ModelResponse>> filterBy(@Param("search") String search,
                                                 @RequestParam(defaultValue = "0") int pageNumber,
                                                 @RequestParam(defaultValue = "100") int size);

    @GetMapping(value = "/find-by-brand-id")
    ResponseEntity<List<ModelResponse>> findByBrandId(@Param("branId") Long brandId);
}
