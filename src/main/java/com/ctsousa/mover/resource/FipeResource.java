package com.ctsousa.mover.resource;

import com.ctsousa.mover.core.api.FipeApi;
import com.ctsousa.mover.response.FipeValueResponse;
import com.ctsousa.mover.service.FipeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/fipe")
public class FipeResource implements FipeApi {

    private final FipeService fipeService;

    public FipeResource(FipeService fipeService) {
        this.fipeService = fipeService;
    }

    @Override
    public ResponseEntity<FipeValueResponse> calculated(String brand, String model, Integer modelYear, String fuelType, LocalDate monthYearReference) {
        FipeValueResponse response = fipeService.calculated(brand, model, fuelType, modelYear, monthYearReference);
        return ResponseEntity.ok(response);
    }
}
