package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.integration.fipe.parallelum.entity.FipeParallelumBrandEntity;
import com.ctsousa.mover.integration.fipe.parallelum.entity.FipeParallelumFipeEntity;
import com.ctsousa.mover.integration.fipe.parallelum.entity.FipeParallelumModelEntity;
import com.ctsousa.mover.integration.fipe.parallelum.entity.FipeParallelumYearEntity;
import com.ctsousa.mover.integration.fipe.parallelum.gateway.FipeParallelumGateway;
import com.ctsousa.mover.response.FipeValueResponse;
import com.ctsousa.mover.service.FipeService;
import org.springframework.stereotype.Component;

@Component
public class FipeServiceImpl implements FipeService {

    private final FipeParallelumGateway gateway;

    public FipeServiceImpl(FipeParallelumGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public FipeValueResponse calculated(String brand, String model, String fuelType, Integer modelYear) {
        try {
            FipeParallelumBrandEntity brandEntity = gateway.findByBrand(brand);
            FipeParallelumModelEntity modelEntity = gateway.findByModel(brandEntity.getCode(), model);
            FipeParallelumYearEntity yearEntity = gateway.findByYear(brandEntity.getCode(), modelEntity.getCode(), modelYear, fuelType);
            FipeParallelumFipeEntity fipeEntity = gateway.findByFipe(brandEntity.getCode(), modelEntity.getCode(), yearEntity.getCode());
            return new FipeValueResponse(fipeEntity.getPrice());
        } catch (Exception e) {
            return new FipeValueResponse("0.00");
        }
    }
}
