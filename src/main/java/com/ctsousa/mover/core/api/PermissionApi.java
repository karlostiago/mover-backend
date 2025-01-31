package com.ctsousa.mover.core.api;

import com.ctsousa.mover.response.FuncionalityResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

public interface PermissionApi {

    @GetMapping("/features")
    ResponseEntity<List<FuncionalityResponse>> findAllFeatures();
}
