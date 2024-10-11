package com.ctsousa.mover.core.api;

import com.ctsousa.mover.response.SubCategoryResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface SubCategoryApi {

    @GetMapping("/findBy")
    ResponseEntity<List<SubCategoryResponse>> findBy(@RequestParam("search") String search);
}
