package com.ctsousa.mover.scheduler;

import com.ctsousa.mover.core.entity.VehicleEntity;
import com.ctsousa.mover.core.util.HashUtil;
import com.ctsousa.mover.integration.fipe.parallelum.entity.FipeParallelumFipeEntity;
import com.ctsousa.mover.integration.fipe.parallelum.gateway.FipeParallelumGateway;
import com.ctsousa.mover.repository.FipeRepository;
import com.ctsousa.mover.service.VehicleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
public class FipeInsertMonthlyScheduler extends FipeBaseScheduler implements Scheduler {

    private final VehicleService vehicleService;

    public FipeInsertMonthlyScheduler(VehicleService vehicleService, FipeParallelumGateway gateway, FipeRepository fipeRepository) {
        super(gateway, fipeRepository);
        this.vehicleService = vehicleService;
    }

    @Override
    @Scheduled(cron = "5 6 1 * * *") // Executa uma vez por mês no dia 1 as 06h05 da manhã
    public void process() {
        List<VehicleEntity> entities = vehicleService.findAll();

        if (entities.isEmpty()) return;

        for (VehicleEntity entity : entities) {
            if (entity.getActive()) {
                String brandName = entity.getBrand().getName();
                String modelName = entity.getModel().getName();
                String fuelType = entity.getFuelType();
                Integer modelYear = entity.getModelYear();
                LocalDate reference = LocalDate.now();

                String context = buildHash(brandName, modelName, fuelType, modelYear, reference);
                hash = HashUtil.buildSHA256(context);

                if (fipeRepository.existsByHash(hash)) return;

                FipeParallelumFipeEntity fipeEntity = findByFipeIntegration(brandName, modelName, fuelType, modelYear, reference);

                if (fipeEntity != null) {
                    saveFipeIntegration(fipeEntity);
                }
            }
        }
    }
}
