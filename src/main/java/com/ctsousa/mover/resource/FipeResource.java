package com.ctsousa.mover.resource;

import com.ctsousa.mover.core.api.FipeApi;
import com.ctsousa.mover.core.entity.VehicleEntity;
import com.ctsousa.mover.core.security.Security;
import com.ctsousa.mover.response.FipeValueResponse;
import com.ctsousa.mover.response.HistoryFipeResponse;
import com.ctsousa.mover.response.SummaryFipeResponse;
import com.ctsousa.mover.service.FipeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

import static com.ctsousa.mover.core.util.DateUtil.toLocalDateWithGMT;

@RestController
@RequestMapping("/fipe")
public class FipeResource implements FipeApi {

    private final FipeService fipeService;

    public FipeResource(FipeService fipeService) {
        this.fipeService = fipeService;
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Vehicle.VIEW_FIPE_VEHICLES)
    public ResponseEntity<SummaryFipeResponse> findByVehicle(Long vehicleId) {
        SummaryFipeResponse response = fipeService.findByVehicle(vehicleId);
        return ResponseEntity.ok(response);
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Vehicle.VIEW_FIPE_VEHICLES)
    public ResponseEntity<FipeValueResponse> calculated(String brand, Integer modelYear, String fuelType, String model, String monthYearReference) {
        LocalDate reference = toLocalDateWithGMT(monthYearReference);
        FipeValueResponse response = fipeService.calculated(brand, model, fuelType, modelYear, reference);
        return ResponseEntity.ok(response);
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Vehicle.VIEW_FIPE_VEHICLES)
    public ResponseEntity<List<HistoryFipeResponse>> history(Long vehicleId) {
        List<HistoryFipeResponse> response = fipeService.history(vehicleId);
        return ResponseEntity.ok(response);
    }
}
