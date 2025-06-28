package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.FipeEntity;
import com.ctsousa.mover.core.entity.VehicleEntity;
import com.ctsousa.mover.core.util.HashUtil;
import com.ctsousa.mover.integration.parallelum.ParallelumGateway;
import com.ctsousa.mover.integration.parallelum.domain.*;
import com.ctsousa.mover.repository.FipeRepository;
import com.ctsousa.mover.response.FipeValueResponse;
import com.ctsousa.mover.response.HistoryFipeResponse;
import com.ctsousa.mover.response.SummaryFipeResponse;
import com.ctsousa.mover.service.FipeService;
import com.ctsousa.mover.service.VehicleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.ctsousa.mover.core.util.NumberUtil.toBigDecimal;
import static com.ctsousa.mover.core.util.StringUtil.removeLastPoint;
import static com.ctsousa.mover.core.util.StringUtil.toUppercase;
import static com.ctsousa.mover.integration.parallelum.util.DateUtil.toMonthPtBr;

@Slf4j
@Component
public class FipeServiceImpl implements FipeService {

    private final ParallelumGateway gateway;
    private final FipeRepository fipeRepository;
    private final VehicleService vehicleService;

    private String hash;

    public FipeServiceImpl(ParallelumGateway gateway, FipeRepository fipeRepository, VehicleService vehicleService) {
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

        String referenceAcquisition = fipeAcquisition.getReferenceMonth().substring(0 ,3) + " / " + fipeAcquisition.getReferenceYear();
        String referenceMonthCurrent = fipeMonthCurrent.getReferenceMonth().substring(0, 3) + " / " + fipeMonthCurrent.getReferenceYear();

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

    @Override
    public List<HistoryFipeResponse> history(Long vehicleId) {
        List<Long> vehiclesId = List.of(vehicleId);
        
        if (vehicleId < 0) {
            vehiclesId = vehicleService.findAll()
                    .stream().map(VehicleEntity::getId)
                    .toList();
        }

        List<HistoryFipeResponse> response = new ArrayList<>();

        for (Long id : vehiclesId) {
            VehicleEntity entity = vehicleService.findById(id);
            List<FipeEntity> entities = fipeRepository.findByHistory(List.of(entity.getId()));
            response.addAll(getHistoryFipeResponse(entities, entity));
        }

        return response;
    }

    private List<HistoryFipeResponse> getHistoryFipeResponse(List<FipeEntity> entities, VehicleEntity vehicleEntity) {
        List<HistoryFipeResponse> response = new ArrayList<>();
        for (FipeEntity entity : entities) {
            HistoryFipeResponse history = new HistoryFipeResponse();
            history.setFipeCode(entity.getCode());
            history.setMonthReference(entity.getReferenceMonth());
            history.setYear(entity.getReferenceYear());
            history.setBrand(entity.getBrand());
            history.setModel(entity.getModel());
            history.setPrice(entity.getPrice());
            history.setVehicleFullname(vehicleEntity.getBrand().getName() + " - " + vehicleEntity.getModel().getName() + " - " + vehicleEntity.getLicensePlate());
            response.add(history);
        }
        return response;
    }

    private FipeValueResponse byIntegration(String brandName, String modelName, String fuelType, Integer modelYear, LocalDate reference) {
        try {
            Brand brand = gateway.findBrand(brandName);
            Model model = gateway.findModel(brand.getCode(), modelName);
            Year year = gateway.findYear(brand.getCode(), model.getCode(), modelYear, fuelType);
            Reference ref = gateway.findReference(reference);
            Fipe fipe = gateway.findFipe(brand.getCode(), model.getCode(), year.getCode(), ref.getCode());
            saveFipe(fipe);
            return new FipeValueResponse(fipe.getPrice(), fipe.getCodeFipe());
        } catch (Exception e) {
            return new FipeValueResponse("0.00", null);
        }
    }

    private void saveFipe(Fipe fipe) {
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
