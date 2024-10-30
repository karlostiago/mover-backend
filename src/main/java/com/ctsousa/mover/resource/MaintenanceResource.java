package com.ctsousa.mover.resource;

import com.ctsousa.mover.core.api.MaintenanceApi;
import com.ctsousa.mover.core.api.resource.BaseResource;
import com.ctsousa.mover.core.entity.MaintenanceEntity;
import com.ctsousa.mover.domain.Maintenance;
import com.ctsousa.mover.request.MaintenanceRequest;
import com.ctsousa.mover.response.MaintenanceResponse;
import com.ctsousa.mover.service.MaintenanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public ResponseEntity<MaintenanceResponse> add(MaintenanceRequest request) {
        Maintenance maintenance = toMapper(request, Maintenance.class);
        MaintenanceEntity entity = maintenanceService.save(maintenance.toEntity());
        return ResponseEntity.ok(toMapper(entity, MaintenanceResponse.class));
    }

    @Override
    public ResponseEntity<MaintenanceResponse> update(Long id, MaintenanceRequest request) {
        maintenanceService.existsById(id);
        Maintenance maintenance = toMapper(request, Maintenance.class);
        MaintenanceEntity entity = maintenance.toEntity();
        maintenanceService.save(entity);
        return ResponseEntity.ok(toMapper(entity, MaintenanceResponse.class));
    }

    @Override
    public ResponseEntity<List<MaintenanceResponse>> filterBy(String search) {
        List<MaintenanceEntity> entities = maintenanceService.filterBy(search);
        return ResponseEntity.ok(toCollection(entities, MaintenanceResponse.class));
    }

    @Override
    public Class<?> responseClass() {
        return MaintenanceResponse.class;
    }
}
