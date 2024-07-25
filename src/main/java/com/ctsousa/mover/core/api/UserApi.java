package com.ctsousa.mover.core.api;

import com.ctsousa.mover.response.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface UserApi {
    @GetMapping("/login/{cpf}/{password}")
    ResponseEntity<UserResponse> login(@PathVariable String cpf, @PathVariable String password);
}
