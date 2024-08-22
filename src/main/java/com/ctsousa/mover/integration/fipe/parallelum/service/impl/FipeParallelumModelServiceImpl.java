package com.ctsousa.mover.integration.fipe.parallelum.service.impl;

import com.ctsousa.mover.integration.fipe.parallelum.FipeParallelumBaseService;
import com.ctsousa.mover.integration.fipe.parallelum.entity.FipeParallelumBrandEntity;
import com.ctsousa.mover.integration.fipe.parallelum.entity.FipeParallelumModelEntity;
import com.ctsousa.mover.integration.fipe.parallelum.service.FipeParallelumModelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class FipeParallelumModelServiceImpl extends FipeParallelumBaseService implements FipeParallelumModelService {

    private static String code = "";

    @Override
    public List<FipeParallelumModelEntity> findBy(FipeParallelumBrandEntity brandEntity) {
        code = brandEntity.getCode();

        log.info("Carregando os modelos, da código da marca {} da api fipe parallelum ", code);

        ParameterizedTypeReference<List<FipeParallelumModelEntity>> responseType = new ParameterizedTypeReference<>() {
        };

        return requestProcess(pathBase(), responseType);
    }

    @Override
    public String pathBase() {
        return "https://fipe.parallelum.com.br/api/v2/cars/brands/" + code + "/models";
    }
}
