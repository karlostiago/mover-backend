package com.ctsousa.mover.core.api;

import com.ctsousa.mover.request.UserRequest;
import com.ctsousa.mover.response.UserResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface UserApi {
    @GetMapping("/login/{cpf}/{password}")
    ResponseEntity<UserResponse> login(@PathVariable String cpf, @PathVariable String password);

    @GetMapping("/filterBy")
    ResponseEntity<List<UserResponse>> filterBy(@RequestParam("search") String search);

    @PutMapping("/changepassword")
    ResponseEntity<Void> changePassword(@RequestBody @Valid UserRequest request);
}
