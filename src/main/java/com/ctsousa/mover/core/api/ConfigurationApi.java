package com.ctsousa.mover.core.api;

import com.ctsousa.mover.response.ParameterResponse;
import com.ctsousa.mover.response.TypeParameterResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ConfigurationApi {

    @GetMapping("/types")
    ResponseEntity<List<TypeParameterResponse>> findAllTypes();

    @GetMapping("/filterBy")
    ResponseEntity<List<ParameterResponse>> filterBy(@RequestParam("search") String search);
}
