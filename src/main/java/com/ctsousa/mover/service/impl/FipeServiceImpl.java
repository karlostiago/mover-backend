package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.integration.fipe.parallelum.entity.*;
import com.ctsousa.mover.integration.fipe.parallelum.gateway.FipeParallelumGateway;
import com.ctsousa.mover.response.FipeValueResponse;
import com.ctsousa.mover.service.FipeService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class FipeServiceImpl implements FipeService {

    private final FipeParallelumGateway gateway;

    public FipeServiceImpl(FipeParallelumGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public FipeValueResponse calculated(String brand, String model, String fuelType, Integer modelYear, LocalDate reference) {
        try {
            FipeParallelumBrandEntity brandEntity = gateway.findByBrand(brand);
            FipeParallelumModelEntity modelEntity = gateway.findByModel(brandEntity.getCode(), model);
            FipeParallelumYearEntity yearEntity = gateway.findByYear(brandEntity.getCode(), modelEntity.getCode(), modelYear, fuelType);
            FipeParallelumReferenceEntity referenceEntity = gateway.findReferenceByMonthAndYear(reference);
            FipeParallelumFipeEntity fipeEntity = gateway.findByFipe(brandEntity.getCode(), modelEntity.getCode(), yearEntity.getCode(), referenceEntity.getCode());
            return new FipeValueResponse(fipeEntity.getPrice());
        } catch (Exception e) {
            return new FipeValueResponse("0.00");
        }
    }
}
