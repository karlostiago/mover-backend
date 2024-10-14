package com.ctsousa.mover.core.api;

import com.ctsousa.mover.response.CategoryResponse;
import com.ctsousa.mover.response.TypeCategoryResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface CategoryApi {

    @GetMapping("/types")
    ResponseEntity<List<TypeCategoryResponse>> findAllTypes();

    @GetMapping("/findBy")
    ResponseEntity<List<CategoryResponse>> findBy(@RequestParam("search") String search);

    @GetMapping("/find-type-category")
    ResponseEntity<List<CategoryResponse>> findByTypeCategory(@RequestParam("type") String type);
}
