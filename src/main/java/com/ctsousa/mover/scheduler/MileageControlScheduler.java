package com.ctsousa.mover.scheduler;

import com.ctsousa.mover.core.entity.MileageControlEntity;
import com.ctsousa.mover.core.entity.VehicleEntity;
import com.ctsousa.mover.integration.corpvs.CorpvsGateway;
import com.ctsousa.mover.integration.corpvs.domain.Vehicle;
import com.ctsousa.mover.repository.MileageControlRepository;
import com.ctsousa.mover.service.VehicleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class MileageControlScheduler implements Scheduler {

    private final VehicleService vehicleService;
    private final CorpvsGateway gateway;
    private final MileageControlRepository repository;

    public MileageControlScheduler(VehicleService vehicleService, CorpvsGateway gateway, MileageControlRepository repository) {
        this.vehicleService = vehicleService;
        this.gateway = gateway;
        this.repository = repository;
    }

    @Override
    @Scheduled(cron = "0 0 1 * * *") // Executa uma vez por dia as 01h00
    public void process() {

        Map<String, VehicleEntity> vehicleMap = vehicleService.findAll()
                .stream()
                .filter(VehicleEntity::getActive)
                .collect(Collectors.toMap(VehicleEntity::getNationalRegistryCode, v -> v));

        List<MileageControlEntity> controls = new ArrayList<>(vehicleMap.size());

        List<Vehicle> vehicles = gateway.allVehicles();
        LocalDate yesterday = LocalDate.now().minusDays(1);

        for (Vehicle vehicle : vehicles) {
            Double odometer = gateway.odometer(vehicle.getVeiId().intValue(), yesterday);
            VehicleEntity entity = vehicleMap.get(vehicle.getVeiRen());
            if (entity != null) {
                MileageControlEntity control = new MileageControlEntity();
                control.setVehicle(entity);
                control.setPeriod(LocalDate.now());
                control.setHour(LocalTime.now());
                control.setOdometer(odometer);
                controls.add(control);

                log.info("Veículo: {} - Odometer: {}", vehicle.getVeiRen(), odometer);
            } else {
                log.warn("Veículo com RENAVAM {} não encontrado no Corpvs", vehicle.getVeiRen());
            }
        }

        controls.forEach(repository::save);
    }
}
