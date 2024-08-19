package com.ctsousa.mover.integration.fipe.parallelum.service.impl;

import com.ctsousa.mover.integration.fipe.parallelum.FipeParallelumBaseService;
import com.ctsousa.mover.integration.fipe.parallelum.entity.FipeParallelumBrandEntity;
import com.ctsousa.mover.integration.fipe.parallelum.entity.FipeParallelumModelEntity;
import com.ctsousa.mover.integration.fipe.parallelum.entity.FipeParallelumYearEntity;
import com.ctsousa.mover.integration.fipe.parallelum.service.FipeParallelumYearService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class FipeParallelumYearServiceImpl extends FipeParallelumBaseService implements FipeParallelumYearService {

    private static String codeBrand = "";
    private static String codeModel = "";

    @Override
    public List<FipeParallelumYearEntity> findBy(FipeParallelumBrandEntity brandEntity, FipeParallelumModelEntity modelEntity) {
        codeBrand = brandEntity.getCode();
        codeModel = modelEntity.getCode();

        log.info("Carregando os anos, da marca {} e modelo {} da api fipe parallelum ", brandEntity.getName(), modelEntity.getName());
        ParameterizedTypeReference<List<FipeParallelumYearEntity>> responseType = new ParameterizedTypeReference<>() {
        };

        return requestProcess(pathBase(), responseType);
    }

    @Override
    public String pathBase() {
        return "http://fipe.parallelum.com.br/api/v2/cars/brands/"+codeBrand+"/models/"+codeModel+"/years";
    }
}
