package com.ctsousa.mover.service;

import com.ctsousa.mover.response.FipeValueResponse;

public interface FipeService {

    FipeValueResponse calculated(String brand, String model, Integer modelYear);
}
