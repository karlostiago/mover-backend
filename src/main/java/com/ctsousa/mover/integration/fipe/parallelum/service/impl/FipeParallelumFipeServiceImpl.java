package com.ctsousa.mover.integration.fipe.parallelum.service.impl;

import com.ctsousa.mover.integration.fipe.parallelum.FipeParallelumBaseService;
import com.ctsousa.mover.integration.fipe.parallelum.entity.FipeParallelumBrandEntity;
import com.ctsousa.mover.integration.fipe.parallelum.entity.FipeParallelumFipeEntity;
import com.ctsousa.mover.integration.fipe.parallelum.entity.FipeParallelumModelEntity;
import com.ctsousa.mover.integration.fipe.parallelum.entity.FipeParallelumYearEntity;
import com.ctsousa.mover.integration.fipe.parallelum.service.FipeParallelumFipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FipeParallelumFipeServiceImpl extends FipeParallelumBaseService implements FipeParallelumFipeService {

    private static String codeBrand = "";
    private static String codeModel = "";
    private static String codeYear = "";

    @Override
    public FipeParallelumFipeEntity findBy(FipeParallelumBrandEntity brandEntity, FipeParallelumModelEntity modelEntity, FipeParallelumYearEntity yearEntity) {
        codeBrand = brandEntity.getCode();
        codeModel = modelEntity.getCode();
        codeYear = yearEntity.getCode();

        log.info("Carregando fipe, da marca {} do modelo {} e ano {} da api fipe parallelum ", brandEntity.getName(), modelEntity.getName(), yearEntity.getName());

        return requestProcess(pathBase(), FipeParallelumFipeEntity.class);
    }

    @Override
    public String pathBase() {
        return "https://fipe.parallelum.com.br/api/v2/cars/brands/"+codeBrand+"/models/"+codeModel+"/years/" + codeYear;
    }
}
