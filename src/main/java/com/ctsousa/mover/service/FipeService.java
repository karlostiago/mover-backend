package com.ctsousa.mover.service;

import com.ctsousa.mover.core.entity.VehicleEntity;
import com.ctsousa.mover.response.FipeValueResponse;
import com.ctsousa.mover.response.HistoryFipeResponse;
import com.ctsousa.mover.response.SummaryFipeResponse;

import java.time.LocalDate;
import java.util.List;

public interface FipeService {

    FipeValueResponse calculated(String brand, String model, String fuelType, Integer modelYear, LocalDate reference);

    SummaryFipeResponse findByVehicle(Long vehicleId);

    List<HistoryFipeResponse> history(Long vehicleId);
}
