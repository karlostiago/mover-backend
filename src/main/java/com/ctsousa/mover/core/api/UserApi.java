package com.ctsousa.mover.core.api;

import com.ctsousa.mover.response.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface UserApi {
    @GetMapping("/login/{cpf}/{password}")
    ResponseEntity<UserResponse> login(@PathVariable String cpf, @PathVariable String password);

    @GetMapping("/filterBy")
    ResponseEntity<List<UserResponse>> filterBy(@RequestParam("search") String search);
}
