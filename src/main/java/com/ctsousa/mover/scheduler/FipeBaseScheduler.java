package com.ctsousa.mover.scheduler;

import com.ctsousa.mover.core.entity.FipeEntity;
import com.ctsousa.mover.integration.fipe.parallelum.entity.*;
import com.ctsousa.mover.integration.fipe.parallelum.gateway.FipeParallelumGateway;
import com.ctsousa.mover.repository.FipeRepository;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

import static com.ctsousa.mover.core.util.NumberUtil.toBigDecimal;
import static com.ctsousa.mover.core.util.StringUtil.removeLastPoint;
import static com.ctsousa.mover.core.util.StringUtil.toUppercase;
import static com.ctsousa.mover.integration.fipe.parallelum.util.DateUtil.toMonthPtBr;

@Slf4j
public abstract class FipeBaseScheduler {

    protected final FipeParallelumGateway gateway;
    protected final FipeRepository fipeRepository;
    protected String hash;

    public FipeBaseScheduler(FipeParallelumGateway gateway, FipeRepository fipeRepository) {
        this.gateway = gateway;
        this.fipeRepository = fipeRepository;
    }

    protected FipeParallelumFipeEntity findByFipeIntegration(String brand, String model, String fuelType, Integer modelYear, LocalDate reference) {
        try {
            FipeParallelumBrandEntity brandEntity = gateway.findByBrand(brand);
            FipeParallelumModelEntity modelEntity = gateway.findByModel(brandEntity.getCode(), model);
            FipeParallelumYearEntity yearEntity = gateway.findByYear(brandEntity.getCode(), modelEntity.getCode(), modelYear, fuelType);
            FipeParallelumReferenceEntity referenceEntity = gateway.findReferenceByMonthAndYear(reference);
            return gateway.findByFipe(brandEntity.getCode(), modelEntity.getCode(), yearEntity.getCode(), referenceEntity.getCode());
        } catch (Exception e) {
            log.error("Erro ao buscar informacoes da fipe na integração ::: {}", e.getMessage());
            return null;
        }
    }

    protected void saveFipeIntegration(FipeParallelumFipeEntity fipeEntity) {
        try {
            FipeEntity entity = new FipeEntity();
            entity.setModel(removeLastPoint(toUppercase(fipeEntity.getModel())));
            entity.setBrand(toUppercase(fipeEntity.getBrand()));
            entity.setCode(fipeEntity.getCodeFipe());
            entity.setModelYear(fipeEntity.getModelYear());
            entity.setReferenceMonth(toUppercase(fipeEntity.getReferenceMonth().split(" ")[0]));
            entity.setReferenceYear(Integer.parseInt(fipeEntity.getReferenceMonth().split(" ")[2]));
            entity.setPrice(toBigDecimal(fipeEntity.getPrice()));
            entity.setFuel(toUppercase(fipeEntity.getFuel()));
            entity.setHash(hash);
            fipeRepository.save(entity);
        } catch (Exception e) {
            log.error("Erro ao salvar informacoes da fipe ::: {}", e.getMessage());
        }
    }

    protected String buildHash(String brand, String model, String fuelType, Integer modelYear, LocalDate reference) {
        return brand.concat(model).concat(fuelType).concat(modelYear.toString()).concat(getMonthYearOfReference(reference));
    }

    protected String getMonthYearOfReference(LocalDate reference) {
        String month = toMonthPtBr(reference);
        int year = reference.getYear();
        return month + "/" + year;
    }
}
