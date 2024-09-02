package com.ctsousa.mover.integration.viacep.gateway;

import com.ctsousa.mover.integration.viacep.entity.ViaCepEntity;

public interface ViaCepGateway {

    ViaCepEntity findByPostalCode(Integer postalCode);
}
