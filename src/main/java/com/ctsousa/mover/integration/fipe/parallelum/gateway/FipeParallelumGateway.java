package com.ctsousa.mover.integration.fipe.parallelum.gateway;

import com.ctsousa.mover.integration.fipe.parallelum.entity.*;

import java.time.LocalDate;
import java.util.List;

public interface FipeParallelumGateway  {

    List<FipeParallelumBrandEntity> listBrands();
    List<FipeParallelumModelEntity> listModels(String codeBrand);
    List<FipeParallelumYearEntity> listYear(String codeBrand, String codeModel);
    List<FipeParallelumReferenceEntity> listReferences();

    FipeParallelumBrandEntity findByBrand(String brandName);
    FipeParallelumYearEntity findByYear(String codeBrand, String codeModel, Integer modelYear, String fuelType);
    FipeParallelumModelEntity findByModel(String codeBrand, String modelName);
    FipeParallelumFipeEntity findByFipe(String codeBrand, String codeModel, String codeYear, String codeReference);
    FipeParallelumReferenceEntity findReferenceByMonthAndYear(LocalDate reference);
}
