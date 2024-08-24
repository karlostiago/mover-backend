package com.ctsousa.mover.core.api;

import com.ctsousa.mover.response.FipeValueResponse;
import com.ctsousa.mover.response.SummaryFipeResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

public interface FipeApi {

    @GetMapping("/find-by/vehicleId/{vehicleId}")
    ResponseEntity<SummaryFipeResponse> findByVehicle(@PathVariable("vehicleId") Long vehicleId);

    @GetMapping("/calculated/brand/{brand}/modelYear/{modelYear}")
    ResponseEntity<FipeValueResponse> calculated(
            @PathVariable String brand,
            @PathVariable Integer modelYear,
            @RequestParam(name = "fuelType") String fuelType,
            @RequestParam(name = "model") String model,
            @RequestParam(name = "reference") String monthYearReference);
}
