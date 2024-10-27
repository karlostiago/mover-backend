package com.ctsousa.mover.core.api;

import com.ctsousa.mover.response.ClientResponse;
import com.ctsousa.mover.response.FuelTypeResponse;
import com.ctsousa.mover.response.SituationResponse;
import com.ctsousa.mover.response.VehicleResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface VehicleApi {

    @GetMapping("/findBy")
    ResponseEntity<List<VehicleResponse>> findBy(@RequestParam("search") String search);

    @GetMapping("/fuel-type")
    ResponseEntity<List<FuelTypeResponse>> findAllFuelType();

    @GetMapping("situation")
    ResponseEntity<List<SituationResponse>> findAllSituation();

    @GetMapping("/only-available")
    ResponseEntity<List<VehicleResponse>> onlyAvailable();
}
