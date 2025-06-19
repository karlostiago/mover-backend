package com.ctsousa.mover.core.api;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface Api<REQUEST, RESPONSE> {

    @PostMapping
    ResponseEntity<RESPONSE> add(@Valid  @RequestBody REQUEST requestBody);

    @Deprecated(forRemoval = true)
    @GetMapping
    ResponseEntity<List<RESPONSE>> findAll();

    @GetMapping("/find-all")
    ResponseEntity<Page<RESPONSE>> findAll(@RequestParam(defaultValue = "0") int currentPage, @RequestParam(defaultValue = "100") int size);

    @GetMapping("/{id}")
    ResponseEntity<RESPONSE> findById(@PathVariable Long id);

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id);

    @PutMapping("/{id}")
    ResponseEntity<RESPONSE> update(@PathVariable Long id, @Valid @RequestBody REQUEST requestBody);
}
