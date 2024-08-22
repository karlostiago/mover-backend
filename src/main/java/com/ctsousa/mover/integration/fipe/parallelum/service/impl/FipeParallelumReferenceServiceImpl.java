package com.ctsousa.mover.integration.fipe.parallelum.service.impl;

import com.ctsousa.mover.integration.fipe.parallelum.FipeParallelumBaseService;
import com.ctsousa.mover.integration.fipe.parallelum.entity.FipeParallelumReferenceEntity;
import com.ctsousa.mover.integration.fipe.parallelum.service.FipeParallelumReferenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class FipeParallelumReferenceServiceImpl extends FipeParallelumBaseService implements FipeParallelumReferenceService {

    @Override
    public List<FipeParallelumReferenceEntity> findAll() {
        log.info("Carregando meses de referencia");

        ParameterizedTypeReference<List<FipeParallelumReferenceEntity>> responseType = new ParameterizedTypeReference<>() { };

        return requestProcess(pathBase(), responseType);
    }

    @Override
    public String pathBase() {
        return "https://fipe.parallelum.com.br/api/v2/references";
    }
}
