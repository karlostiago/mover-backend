package com.ctsousa.mover.integration.viacep.service;

import com.ctsousa.mover.integration.viacep.entity.ViaCepEntity;
import org.springframework.stereotype.Service;

@Service
public interface ViaCepService {

    ViaCepEntity findByPostalCode(Integer postalCode);
}
