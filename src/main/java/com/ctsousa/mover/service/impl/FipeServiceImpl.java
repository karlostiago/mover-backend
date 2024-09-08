package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.FipeEntity;
import com.ctsousa.mover.core.entity.VehicleEntity;
import com.ctsousa.mover.core.util.HashUtil;
import com.ctsousa.mover.integration.fipe.parallelum.entity.*;
import com.ctsousa.mover.integration.fipe.parallelum.gateway.FipeParallelumGateway;
import com.ctsousa.mover.repository.FipeRepository;
import com.ctsousa.mover.response.FipeValueResponse;
import com.ctsousa.mover.response.SummaryFipeResponse;
import com.ctsousa.mover.service.FipeService;
import com.ctsousa.mover.service.VehicleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static com.ctsousa.mover.core.util.NumberUtil.toBigDecimal;
import static com.ctsousa.mover.core.util.StringUtil.removeLastPoint;
import static com.ctsousa.mover.core.util.StringUtil.toUppercase;
import static com.ctsousa.mover.integration.fipe.parallelum.util.DateUtil.toMonthPtBr;

@Slf4j
@Component
public class FipeServiceImpl implements FipeService {

    private final FipeParallelumGateway gateway;
    private final FipeRepository fipeRepository;
    private final VehicleService vehicleService;

    private String hash;

    public FipeServiceImpl(FipeParallelumGateway gateway, FipeRepository fipeRepository, VehicleService vehicleService) {
        this.gateway = gateway;
        this.fipeRepository = fipeRepository;
        this.vehicleService = vehicleService;
    }

    @Override
    public SummaryFipeResponse findByVehicle(Long vehicleId) {
        VehicleEntity vehicleEntity = vehicleService.findById(vehicleId);

        FipeEntity fipeAcquisition = fipeRepository.findByVehicleAndReference(vehicleId, vehicleEntity.getAcquisitionDate());
        FipeEntity fipeMonthCurrent = fipeRepository.findByVehicleAndReference(vehicleId, LocalDate.now());

        if (fipeAcquisition == null || fipeMonthCurrent == null) {
            return new SummaryFipeResponse(BigDecimal.ZERO, null, BigDecimal.ZERO, null);
        }

        String referenceAcquisition = fipeAcquisition.getReferenceMonth() + " / " + fipeAcquisition.getReferenceYear();
        String referenceMonthCurrent = fipeMonthCurrent.getReferenceMonth() + " / " + fipeMonthCurrent.getReferenceYear();

        return calculatedSummaryFipe(new SummaryFipeResponse(fipeAcquisition.getPrice(), referenceAcquisition, fipeMonthCurrent.getPrice(), referenceMonthCurrent));
    }

    @Override
    public FipeValueResponse calculated(String brand, String model, String fuelType, Integer modelYear, LocalDate reference) {
        String context = buildHash(brand, model, fuelType, modelYear, reference);
        hash = HashUtil.buildSHA256(context);
        FipeEntity entity = fipeRepository.findByHash(hash);

        if (entity == null) {
            return byIntegration(brand, model, fuelType, modelYear, reference);
        }

        return new FipeValueResponse(entity.getPrice(), entity.getCode());
    }

    private FipeValueResponse byIntegration(String brand, String model, String fuelType, Integer modelYear, LocalDate reference) {
        try {
            FipeParallelumBrandEntity brandEntity = gateway.findByBrand(brand);
            FipeParallelumModelEntity modelEntity = gateway.findByModel(brandEntity.getCode(), model);
            FipeParallelumYearEntity yearEntity = gateway.findByYear(brandEntity.getCode(), modelEntity.getCode(), modelYear, fuelType);
            FipeParallelumReferenceEntity referenceEntity = gateway.findReferenceByMonthAndYear(reference);
            FipeParallelumFipeEntity fipeEntity = gateway.findByFipe(brandEntity.getCode(), modelEntity.getCode(), yearEntity.getCode(), referenceEntity.getCode());
            saveFipe(fipeEntity);
            return new FipeValueResponse(fipeEntity.getPrice(), fipeEntity.getCodeFipe());
        } catch (Exception e) {
            return new FipeValueResponse("0.00", null);
        }
    }

    private void saveFipe(FipeParallelumFipeEntity fipeEntity) {
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

    private static String getMonthYearOfReference(LocalDate reference) {
        String month = toMonthPtBr(reference);
        int year = reference.getYear();
        return month + "/" + year;
    }

    private String buildHash(String brand, String model, String fuelType, Integer modelYear, LocalDate reference) {
        return brand.concat(model).concat(fuelType).concat(modelYear.toString()).concat(getMonthYearOfReference(reference));
    }

    private SummaryFipeResponse calculatedSummaryFipe(SummaryFipeResponse summaryFipeResponse) {
        summaryFipeResponse.setDepreciatedValue(summaryFipeResponse.getValueMonthCurrent().subtract(summaryFipeResponse.getValueAcquisition()));
        summaryFipeResponse.setPercentageDepreciated(summaryFipeResponse.getValueMonthCurrent()
                .subtract(summaryFipeResponse.getValueAcquisition())
                .divide(summaryFipeResponse.getValueAcquisition(), 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100D))
                .doubleValue());
        return summaryFipeResponse;
    }
}
