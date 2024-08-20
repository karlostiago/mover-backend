package com.ctsousa.mover.integration.fipe.parallelum.gateway.impl;

import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.integration.fipe.parallelum.entity.FipeParallelumBrandEntity;
import com.ctsousa.mover.integration.fipe.parallelum.entity.FipeParallelumFipeEntity;
import com.ctsousa.mover.integration.fipe.parallelum.entity.FipeParallelumModelEntity;
import com.ctsousa.mover.integration.fipe.parallelum.entity.FipeParallelumYearEntity;
import com.ctsousa.mover.integration.fipe.parallelum.gateway.FipeParallelumGateway;
import com.ctsousa.mover.integration.fipe.parallelum.service.FipeParallelumBrandService;
import com.ctsousa.mover.integration.fipe.parallelum.service.FipeParallelumFipeService;
import com.ctsousa.mover.integration.fipe.parallelum.service.FipeParallelumModelService;
import com.ctsousa.mover.integration.fipe.parallelum.service.FipeParallelumYearService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FipeParallelumGatewayImpl implements FipeParallelumGateway {

    private final FipeParallelumBrandService fipeParallelumBrandService;
    private final FipeParallelumModelService fipeParallelumModelService;
    private final FipeParallelumYearService fipeParallelumYearService;
    private final FipeParallelumFipeService fipeParallelumFipeService;

    public FipeParallelumGatewayImpl(FipeParallelumBrandService fipeParallelumBrandService, FipeParallelumModelService fipeParallelumModelService, FipeParallelumYearService fipeParallelumYearService, FipeParallelumFipeService fipeParallelumFipeService) {
        this.fipeParallelumBrandService = fipeParallelumBrandService;
        this.fipeParallelumModelService = fipeParallelumModelService;
        this.fipeParallelumYearService = fipeParallelumYearService;
        this.fipeParallelumFipeService = fipeParallelumFipeService;
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
    public FipeParallelumFipeEntity findByFipe(String codeBrand, String codeModel, String codeYear) {
        return fipeParallelumFipeService.findBy(new FipeParallelumBrandEntity(codeBrand),
                new FipeParallelumModelEntity(codeModel), new FipeParallelumYearEntity(codeYear));
    }

    @Override
    public FipeParallelumBrandEntity findByBrand(String brandName) {
        return listBrands().stream()
                .filter(b -> b.getName().equalsIgnoreCase(brandName))
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
}
