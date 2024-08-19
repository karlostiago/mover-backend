package com.ctsousa.mover.integration.fipe.parallelum.gateway;

import com.ctsousa.mover.integration.fipe.parallelum.entity.FipeParallelumBrandEntity;
import com.ctsousa.mover.integration.fipe.parallelum.entity.FipeParallelumFipeEntity;
import com.ctsousa.mover.integration.fipe.parallelum.entity.FipeParallelumModelEntity;
import com.ctsousa.mover.integration.fipe.parallelum.entity.FipeParallelumYearEntity;

import java.util.List;

public interface FipeParallelumGateway {

    List<FipeParallelumBrandEntity> listBrands();
    List<FipeParallelumModelEntity> listModels(String codeBrand);
    List<FipeParallelumYearEntity> listYear(String codeBrand, String codeModel);
    FipeParallelumFipeEntity findBy(String codeBrand, String codeModel, String codeYear);
}