package com.ctsousa.mover.integration.viacep.service.impl;

import com.ctsousa.mover.integration.RestBaseServiceHttp;
import com.ctsousa.mover.integration.viacep.entity.ViaCepEntity;
import com.ctsousa.mover.integration.viacep.service.ViaCepService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ViaCepServiceImpl extends RestBaseServiceHttp implements ViaCepService {

    private static Integer postalCode;

    @Override
    public ViaCepEntity findByPostalCode(Integer postalCode) {
        ViaCepServiceImpl.postalCode = postalCode;

        log.info("Buscando cep {} pela integracao viacep ", postalCode);

        return requestProcess(pathBase(), ViaCepEntity.class);
    }

    public String pathBase() {
        return "https://viacep.com.br/ws/"+ postalCode +"/json/";
    }
}
