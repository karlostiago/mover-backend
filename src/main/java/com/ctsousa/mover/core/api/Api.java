package com.ctsousa.mover.core.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface Api<REQUEST, RESPONSE> {

    @PostMapping
    ResponseEntity<RESPONSE> add(@RequestBody REQUEST requestBody);

    @GetMapping
    ResponseEntity<List<RESPONSE>> findAll();

    @GetMapping("/{id}")
    ResponseEntity<RESPONSE> findById(@PathVariable Long id);

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id);

    @PutMapping("/{id}")
    ResponseEntity<RESPONSE> update(@PathVariable Long id, @RequestBody REQUEST requestBody);
}
