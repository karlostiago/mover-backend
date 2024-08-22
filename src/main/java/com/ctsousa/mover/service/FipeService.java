package com.ctsousa.mover.service;

import com.ctsousa.mover.response.FipeValueResponse;

import java.time.LocalDate;

public interface FipeService {

    FipeValueResponse calculated(String brand, String model, String fuelType, Integer modelYear, LocalDate reference);
}
