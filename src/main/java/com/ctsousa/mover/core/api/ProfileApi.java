package com.ctsousa.mover.core.api;

import com.ctsousa.mover.response.ProfileResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ProfileApi {

    @GetMapping("/filterBy")
    ResponseEntity<List<ProfileResponse>> filterBy(@RequestParam("search") String search);
}
