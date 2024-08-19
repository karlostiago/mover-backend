package com.ctsousa.mover.integration.fipe.parallelum.service.impl;

import com.ctsousa.mover.integration.fipe.parallelum.FipeParallelumBaseService;
import com.ctsousa.mover.integration.fipe.parallelum.entity.FipeParallelumBrandEntity;
import com.ctsousa.mover.integration.fipe.parallelum.service.FipeParallelumBrandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class FipeParallelumBrandServiceImpl extends FipeParallelumBaseService implements FipeParallelumBrandService {

    @Override
    public List<FipeParallelumBrandEntity> findAll() {
        log.info("Carregando as marcas da api fipe parallelum");

        ParameterizedTypeReference<List<FipeParallelumBrandEntity>> responseType = new ParameterizedTypeReference<>() {
        };

        return requestProcess(pathBase(), responseType);
    }

    @Override
    public String pathBase() {
        return "https://fipe.parallelum.com.br/api/v2/cars/brands";
    }
}
