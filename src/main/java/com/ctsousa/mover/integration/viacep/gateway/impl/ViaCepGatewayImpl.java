package com.ctsousa.mover.integration.viacep.gateway.impl;

import com.ctsousa.mover.integration.viacep.entity.ViaCepEntity;
import com.ctsousa.mover.integration.viacep.gateway.ViaCepGateway;
import com.ctsousa.mover.integration.viacep.service.ViaCepService;
import org.springframework.stereotype.Component;

@Component
public class ViaCepGatewayImpl implements ViaCepGateway {

    private final ViaCepService service;

    public ViaCepGatewayImpl(ViaCepService service) {
        this.service = service;
    }

    @Override
    public ViaCepEntity findByPostalCode(Integer postalCode) {
        return service.findByPostalCode(postalCode);
    }
}
