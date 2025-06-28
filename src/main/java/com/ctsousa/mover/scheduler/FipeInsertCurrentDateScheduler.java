package com.ctsousa.mover.scheduler;

import com.ctsousa.mover.core.entity.VehicleEntity;
import com.ctsousa.mover.core.util.HashUtil;
import com.ctsousa.mover.integration.parallelum.ParallelumGateway;
import com.ctsousa.mover.integration.parallelum.domain.Fipe;
import com.ctsousa.mover.repository.FipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
@Component
public class FipeInsertCurrentDateScheduler extends FipeBaseScheduler implements Scheduler {

    public static final Queue<VehicleEntity> buffers = new ConcurrentLinkedQueue<>();

    public FipeInsertCurrentDateScheduler(FipeRepository fipeRepository, ParallelumGateway parallelumGateway) {
        super(fipeRepository, parallelumGateway);
    }

    @Override
    @Scheduled(cron = "0/10 * * * * *") // executa a cada 10 segundos
    public void process() {
        if (buffers.isEmpty()) return;

        while (!buffers.isEmpty()) {
            VehicleEntity entity = buffers.poll();
            String brandName = entity.getBrand().getName();
            String modelName = entity.getModel().getName();
            String fuelType = entity.getFuelType();
            Integer modelYear = entity.getModelYear();
            LocalDate reference = LocalDate.now();

            String context = buildHash(brandName, modelName, fuelType, modelYear, reference);
            hash = HashUtil.buildSHA256(context);

            if (fipeRepository.existsByHash(hash)) return;

            Fipe fipe = findByFipeIntegration(brandName, modelName, fuelType, modelYear, reference);

            if (fipe != null) {
                saveFipeIntegration(fipe);
            }
        }
    }
}
