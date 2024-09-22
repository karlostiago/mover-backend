package com.ctsousa.mover.core.api;

import com.ctsousa.mover.response.ConfigurationResponse;
import com.ctsousa.mover.response.TypeConfigurationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ConfigurationApi {

    @GetMapping("/types")
    ResponseEntity<List<TypeConfigurationResponse>> findAllTypes();

    @GetMapping("/filterBy")
    ResponseEntity<List<ConfigurationResponse>> filterBy(@RequestParam("search") String search);
}
