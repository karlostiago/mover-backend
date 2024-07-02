package com.ctsousa.mover.core.api;

import com.ctsousa.mover.domain.Brand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface BrandApi {

    @PostMapping
    ResponseEntity<Brand> add(@RequestBody Brand brand);

    @GetMapping
    ResponseEntity<List<Brand>> findAll();

    @GetMapping("/{id}")
    ResponseEntity<Brand> findById(@PathVariable Long id);

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id);
}
