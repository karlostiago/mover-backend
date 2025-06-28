package com.ctsousa.mover.scheduler;

import com.ctsousa.mover.core.entity.FipeEntity;
import com.ctsousa.mover.integration.parallelum.ParallelumGateway;
import com.ctsousa.mover.integration.parallelum.domain.*;
import com.ctsousa.mover.repository.FipeRepository;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

import static com.ctsousa.mover.core.util.NumberUtil.toBigDecimal;
import static com.ctsousa.mover.core.util.StringUtil.removeLastPoint;
import static com.ctsousa.mover.core.util.StringUtil.toUppercase;
import static com.ctsousa.mover.integration.parallelum.util.DateUtil.toMonthPtBr;

@Slf4j
public abstract class FipeBaseScheduler {

    protected final FipeRepository fipeRepository;
    protected final ParallelumGateway parallelumGateway;
    protected String hash;

    public FipeBaseScheduler(FipeRepository fipeRepository, ParallelumGateway parallelumGateway) {
        this.fipeRepository = fipeRepository;
        this.parallelumGateway = parallelumGateway;
    }

    protected Fipe findByFipeIntegration(String brandName, String modelName, String fuelType, Integer modelYear, LocalDate reference) {
        try {
            Brand brand = parallelumGateway.findBrand(brandName);
            Model model = parallelumGateway.findModel(brand.getCode(), modelName);
            Year year = parallelumGateway.findYear(brand.getCode(), model.getCode(), modelYear, fuelType);
            Reference ref = parallelumGateway.findReference(reference);
            return parallelumGateway.findFipe(brand.getCode(), model.getCode(), year.getCode(), ref.getCode());
        } catch (Exception e) {
            log.error("Erro ao buscar informacoes da fipe na integração ::: {}", e.getMessage());
            return null;
        }
    }

    protected void saveFipeIntegration(Fipe fipe) {
        try {
            FipeEntity entity = new FipeEntity();
            entity.setModel(removeLastPoint(toUppercase(fipe.getModel())));
            entity.setBrand(toUppercase(fipe.getBrand()));
            entity.setCode(fipe.getCodeFipe());
            entity.setModelYear(fipe.getModelYear());
            entity.setReferenceMonth(toUppercase(fipe.getReferenceMonth().split(" ")[0]));
            entity.setReferenceYear(Integer.parseInt(fipe.getReferenceMonth().split(" ")[2]));
            entity.setPrice(toBigDecimal(fipe.getPrice()));
            entity.setFuel(toUppercase(fipe.getFuel()));
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
