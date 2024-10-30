package com.ctsousa.mover.core.api;

import com.ctsousa.mover.response.MaintenanceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface MaintenanceApi {

    @GetMapping("/filterBy")
    ResponseEntity<List<MaintenanceResponse>> filterBy(@RequestParam("search") String search);
}
