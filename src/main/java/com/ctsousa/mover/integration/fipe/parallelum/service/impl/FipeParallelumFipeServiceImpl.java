package com.ctsousa.mover.integration.fipe.parallelum.service.impl;

import com.ctsousa.mover.integration.fipe.parallelum.FipeParallelumBaseService;
import com.ctsousa.mover.integration.fipe.parallelum.entity.*;
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
    private static String codeReference = "";

    @Override
    public FipeParallelumFipeEntity findBy(FipeParallelumBrandEntity brandEntity, FipeParallelumModelEntity modelEntity, FipeParallelumYearEntity yearEntity, FipeParallelumReferenceEntity referenceEntity) {
        codeBrand = brandEntity.getCode();
        codeModel = modelEntity.getCode();
        codeYear = yearEntity.getCode();
        codeReference = referenceEntity.getCode();

        log.info("Carregando fipe, da código da marca {} do código do modelo {} e código do ano {} da api fipe parallelum ", codeBrand, codeModel, codeYear);

        return requestProcess(pathBase(), FipeParallelumFipeEntity.class);
    }

    @Override
    public String pathBase() {
        return "https://fipe.parallelum.com.br/api/v2/cars/brands/"+codeBrand+"/models/"+codeModel+"/years/" + codeYear + "?reference=" + codeReference;
    }
}
