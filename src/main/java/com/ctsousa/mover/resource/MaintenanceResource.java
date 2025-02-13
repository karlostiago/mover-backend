package com.ctsousa.mover.resource;

import com.ctsousa.mover.core.api.MaintenanceApi;
import com.ctsousa.mover.core.api.resource.BaseResource;
import com.ctsousa.mover.core.entity.MaintenanceEntity;
import com.ctsousa.mover.core.security.Security;
import com.ctsousa.mover.domain.Maintenance;
import com.ctsousa.mover.enumeration.TypeMaintenance;
import com.ctsousa.mover.request.MaintenanceRequest;
import com.ctsousa.mover.response.MaintenanceResponse;
import com.ctsousa.mover.response.TypeMaintenanceResponse;
import com.ctsousa.mover.service.MaintenanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.ctsousa.mover.core.mapper.Transform.toCollection;
import static com.ctsousa.mover.core.mapper.Transform.toMapper;

@RestController
@RequestMapping("/maintenance")
public class MaintenanceResource extends BaseResource<MaintenanceResponse, MaintenanceRequest, MaintenanceEntity> implements MaintenanceApi {

    @Autowired
    private MaintenanceService maintenanceService;

    public MaintenanceResource(MaintenanceService maintenanceService) {
        super(maintenanceService);
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Maintenance.REGISTER_MAINTENANCE)
    public ResponseEntity<MaintenanceResponse> add(MaintenanceRequest request) {
        Maintenance maintenance = toMapper(request, Maintenance.class);
        MaintenanceEntity entity = maintenanceService.save(maintenance.toEntity());
        return ResponseEntity.ok(toMapper(entity, MaintenanceResponse.class));
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Maintenance.UPDATE_MAINTENANCE)
    public ResponseEntity<MaintenanceResponse> update(Long id, MaintenanceRequest request) {
        maintenanceService.existsById(id);
        Maintenance maintenance = toMapper(request, Maintenance.class);
        MaintenanceEntity entity = maintenance.toEntity();
        maintenanceService.save(entity);
        return ResponseEntity.ok(toMapper(entity, MaintenanceResponse.class));
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Maintenance.FILTER_MAINTENANCE)
    public ResponseEntity<List<MaintenanceResponse>> filterBy(String search) {
        List<MaintenanceEntity> entities = maintenanceService.filterBy(search);
        List<MaintenanceResponse> response = toCollection(entities, MaintenanceResponse.class);
        updateResponse(response, entities);
        return ResponseEntity.ok(response);
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Maintenance.FILTER_MAINTENANCE)
    public ResponseEntity<List<TypeMaintenanceResponse>> findAllTypes() {
        List<TypeMaintenance> types = Stream.of(TypeMaintenance.values())
                .sorted(Comparator.comparing(TypeMaintenance::getCode))
                .toList();
        return ResponseEntity.ok(toCollection(types, TypeMaintenanceResponse.class));
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Maintenance.DELETE_MAINTENANCE)
    public void delete(Long id) {
        super.delete(id);
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Maintenance.FILTER_MAINTENANCE)
    public ResponseEntity<List<MaintenanceResponse>> findAll() {
        return super.findAll();
    }

    @Override
    public void updateResponse(List<MaintenanceResponse> response, List<MaintenanceEntity> entities) {
        Map<Long, MaintenanceResponse> responseMap = response.stream()
                .collect(Collectors.toMap(MaintenanceResponse::getId, r -> r));

        for (MaintenanceEntity entity : entities) {
            String fullNameVehicle = entity.getVehicle().getBrand().getName() + " - " +
                    entity.getVehicle().getModel().getName() + " - " +
                    entity.getVehicle().getLicensePlate();
            MaintenanceResponse maintenanceResponse = responseMap.get(entity.getId());
            maintenanceResponse.setVehicleName(fullNameVehicle);
        }
    }

    @Override
    public Class<?> responseClass() {
        return MaintenanceResponse.class;
    }
}
