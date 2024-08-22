package com.ctsousa.mover.core.api;

import com.ctsousa.mover.response.FipeValueResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;

public interface FipeApi {

    @GetMapping("/calculated/brand/{brand}/model/{model}/modelYear/{modelYear}/fuelType/{fuelType}/reference/{monthYearReference}")
    ResponseEntity<FipeValueResponse> calculated(@PathVariable String brand, @PathVariable String model, @PathVariable Integer modelYear, @PathVariable String fuelType, @PathVariable String monthYearReference);
}
