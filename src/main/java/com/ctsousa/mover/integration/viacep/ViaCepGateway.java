package com.ctsousa.mover.integration.viacep;

import com.ctsousa.mover.integration.viacep.domain.ViaCep;

public interface ViaCepGateway {
    ViaCep findPostalCode(Integer postalCode);
}
