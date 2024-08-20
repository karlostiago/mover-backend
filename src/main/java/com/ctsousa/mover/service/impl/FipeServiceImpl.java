package com.ctsousa.mover.service.impl;

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
    public FipeValueResponse calculated(String brand, String model, Integer modelYear) {
        return null;
    }
}
