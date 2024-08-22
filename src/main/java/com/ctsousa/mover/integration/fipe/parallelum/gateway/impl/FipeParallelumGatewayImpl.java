package com.ctsousa.mover.integration.fipe.parallelum.gateway.impl;

import com.ctsousa.mover.integration.fipe.parallelum.entity.*;
import com.ctsousa.mover.integration.fipe.parallelum.gateway.FipeParallelumGateway;
import com.ctsousa.mover.integration.fipe.parallelum.service.*;
import com.ctsousa.mover.integration.fipe.parallelum.util.DateUtil;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class FipeParallelumGatewayImpl implements FipeParallelumGateway {

    private final FipeParallelumBrandService fipeParallelumBrandService;
    private final FipeParallelumModelService fipeParallelumModelService;
    private final FipeParallelumYearService fipeParallelumYearService;
    private final FipeParallelumFipeService fipeParallelumFipeService;
    private final FipeParallelumReferenceService fipeParallelumReferenceService;

    public FipeParallelumGatewayImpl(FipeParallelumBrandService fipeParallelumBrandService, FipeParallelumModelService fipeParallelumModelService, FipeParallelumYearService fipeParallelumYearService, FipeParallelumFipeService fipeParallelumFipeService, FipeParallelumReferenceService fipeParallelumReferenceService) {
        this.fipeParallelumBrandService = fipeParallelumBrandService;
        this.fipeParallelumModelService = fipeParallelumModelService;
        this.fipeParallelumYearService = fipeParallelumYearService;
        this.fipeParallelumFipeService = fipeParallelumFipeService;
        this.fipeParallelumReferenceService = fipeParallelumReferenceService;
    }

    @Override
    public List<FipeParallelumBrandEntity> listBrands() {
        return fipeParallelumBrandService.findAll();
    }

    @Override
    public List<FipeParallelumModelEntity> listModels(String codeBrand) {
        return fipeParallelumModelService.findBy(new FipeParallelumBrandEntity(codeBrand));
    }

    @Override
    public List<FipeParallelumYearEntity> listYear(String codeBrand, String codeModel) {
        return fipeParallelumYearService.findBy(new FipeParallelumBrandEntity(codeBrand), new FipeParallelumModelEntity(codeModel));
    }

    @Override
    public List<FipeParallelumReferenceEntity> listReferences() {
        return fipeParallelumReferenceService.findAll();
    }

    @Override
    public FipeParallelumFipeEntity findByFipe(String codeBrand, String codeModel, String codeYear, String codeReference) {
        return fipeParallelumFipeService.findBy(new FipeParallelumBrandEntity(codeBrand),
                new FipeParallelumModelEntity(codeModel), new FipeParallelumYearEntity(codeYear), new FipeParallelumReferenceEntity(codeReference));
    }

    @Override
    public FipeParallelumBrandEntity findByBrand(String brandName) {
        return listBrands().stream()
                .filter(b -> b.getName().toUpperCase().contains(brandName))
                .findFirst()
                .orElse(null);
    }

    @Override
    public FipeParallelumModelEntity findByModel(String codeBrand, String modelName) {
        return listModels(codeBrand).stream()
                .filter(m -> m.getName().equalsIgnoreCase(modelName))
                .findFirst()
                .orElse(null);
    }

    @Override
    public FipeParallelumYearEntity findByYear(String codeBrand, String codeModel, Integer modelYear, String fuelType) {
        String nameFilter = modelYear.toString().concat(" ").concat(fuelType);
        return listYear(codeBrand, codeModel).stream()
                .filter(y -> y.getName().equalsIgnoreCase(nameFilter))
                .findFirst()
                .orElse(null);
    }

    @Override
    public FipeParallelumReferenceEntity findReferenceByMonthAndYear(LocalDate reference) {
        String monthAndYearOfReference = getMonthYearOfReference(reference);
        return listReferences().stream()
                .filter(r -> r.getMonth().equalsIgnoreCase(monthAndYearOfReference))
                .findFirst()
                .orElse(null);
    }

    private String getMonthYearOfReference(LocalDate reference) {
        String month = DateUtil.toMonthPtBr(reference);
        int year = reference.getYear();
        return month + "/" + year;
    }
}
