package com.ctsousa.mover.core.api;

import com.ctsousa.mover.response.FipeValueResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface FipeApi {

    @GetMapping("/calculated/brand/{brand}/model/{model}/modelYear/{modelYear}/fuelType/{fuelType}")
    ResponseEntity<FipeValueResponse> calculated(@PathVariable String brand, @PathVariable String model, @PathVariable Integer modelYear, @PathVariable String fuelType);
}
