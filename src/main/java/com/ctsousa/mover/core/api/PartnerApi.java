package com.ctsousa.mover.core.api;

import com.ctsousa.mover.response.PartnerResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface PartnerApi {

    @GetMapping("/filterBy")
    ResponseEntity<List<PartnerResponse>> filterBy(@RequestParam("search") String search);
}
