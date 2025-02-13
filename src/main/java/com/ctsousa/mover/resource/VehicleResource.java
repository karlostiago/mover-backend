package com.ctsousa.mover.resource;

import com.ctsousa.mover.core.api.VehicleApi;
import com.ctsousa.mover.core.api.resource.BaseResource;
import com.ctsousa.mover.core.entity.VehicleEntity;
import com.ctsousa.mover.core.security.Security;
import com.ctsousa.mover.domain.Vehicle;
import com.ctsousa.mover.enumeration.FuelType;
import com.ctsousa.mover.enumeration.Situation;
import com.ctsousa.mover.request.VehicleRequest;
import com.ctsousa.mover.response.FuelTypeResponse;
import com.ctsousa.mover.response.SituationResponse;
import com.ctsousa.mover.response.VehicleResponse;
import com.ctsousa.mover.scheduler.FipeInsertCurrentDateScheduler;
import com.ctsousa.mover.service.BrandService;
import com.ctsousa.mover.service.ModelService;
import com.ctsousa.mover.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.ctsousa.mover.core.mapper.Transform.toCollection;
import static com.ctsousa.mover.core.mapper.Transform.toMapper;

@RestController
@RequestMapping("/vehicles")
public class VehicleResource extends BaseResource<VehicleResponse, VehicleRequest, VehicleEntity> implements VehicleApi {

    @Autowired
    private VehicleService vehicleService;
    private final BrandService brandService;
    private final ModelService modelService;

    public VehicleResource(VehicleService vehicleService, BrandService brandService, ModelService modelService) {
        super(vehicleService);
        this.brandService = brandService;
        this.modelService = modelService;
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Vehicle.REGISTER_VEHICLES)
    public ResponseEntity<VehicleResponse> add(VehicleRequest request) {
        Vehicle domain = toMapper(request, Vehicle.class);
        VehicleEntity entity = domain.toEntity();
        entity.setBrand(brandService.findById(domain.getBrand().getId()));
        entity.setModel(modelService.findById(domain.getModel().getId()));
        entity = vehicleService.save(entity);
        FipeInsertCurrentDateScheduler.buffers.add(entity);
        return ResponseEntity.ok(toMapper(entity, VehicleResponse.class));
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Vehicle.UPDATE_VEHICLES)
    public ResponseEntity<VehicleResponse> update(Long id, VehicleRequest request) {
        vehicleService.existsById(id);
        Vehicle domain = toMapper(request, Vehicle.class);
        VehicleEntity entity = domain.toEntity();
        entity.setId(id);
        entity.setBrand(brandService.findById(domain.getBrand().getId()));
        entity.setModel(modelService.findById(domain.getModel().getId()));
        vehicleService.save(entity);
        return ResponseEntity.ok(toMapper(entity, VehicleResponse.class));
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Vehicle.FILTER_VEHICLES)
    public ResponseEntity<List<VehicleResponse>> findBy(String search) {
        List<VehicleEntity> entities = vehicleService.findBy(search);
        return ResponseEntity.ok(toCollection(entities, VehicleResponse.class));
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Vehicle.FILTER_VEHICLES)
    public ResponseEntity<List<FuelTypeResponse>> findAllFuelType() {
        List<FuelType> types = List.of(FuelType.values());
        return ResponseEntity.ok(toCollection(types, FuelTypeResponse.class));
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Vehicle.FILTER_VEHICLES)
    public ResponseEntity<List<SituationResponse>> findAllSituation() {
        List<Situation> situations = List.of(Situation.IN_FLEET, Situation.SOLD, Situation.TOTAL_LOSS, Situation.IN_ACQUISITION);
        return ResponseEntity.ok(toCollection(situations, SituationResponse.class));
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Vehicle.FILTER_VEHICLES)
    public ResponseEntity<List<VehicleResponse>> onlyAvailable() {
        List<VehicleEntity> entities = vehicleService.onlyAvailable();
        return ResponseEntity.ok(toCollection(entities, VehicleResponse.class));
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Vehicle.DELETE_VEHICLES)
    public void delete(Long id) {
        super.delete(id);
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Vehicle.FILTER_VEHICLES)
    public ResponseEntity<List<VehicleResponse>> findAll() {
        return super.findAll();
    }

    @Override
    public Class<?> responseClass() {
        return VehicleResponse.class;
    }
}
