package com.ctsousa.mover.resource;

import com.ctsousa.mover.core.api.VehicleApi;
import com.ctsousa.mover.core.entity.VehicleEntity;
import com.ctsousa.mover.domain.Vehicle;
import com.ctsousa.mover.request.VehicleRequest;
import com.ctsousa.mover.response.VehicleResponse;
import com.ctsousa.mover.service.BrandService;
import com.ctsousa.mover.service.ModelService;
import com.ctsousa.mover.service.VehicleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.ctsousa.mover.core.mapper.Transform.toCollection;
import static com.ctsousa.mover.core.mapper.Transform.toMapper;

@RestController
@RequestMapping("/vehicles")
public class VehicleResource implements VehicleApi {

    private final VehicleService vehicleService;
    private final BrandService brandService;
    private final ModelService modelService;

    public VehicleResource(VehicleService vehicleService, BrandService brandService, ModelService modelService) {
        this.vehicleService = vehicleService;
        this.brandService = brandService;
        this.modelService = modelService;
    }

    @Override
    public ResponseEntity<VehicleResponse> add(VehicleRequest request) {
        Vehicle domain = toMapper(request, Vehicle.class);
        VehicleEntity entity = domain.toEntity();
        entity.setBrand(brandService.findById(domain.getBrand().getId()));
        entity.setModel(modelService.findById(domain.getModel().getId()));
        entity = vehicleService.save(entity);
        return ResponseEntity.ok(toMapper(entity, VehicleResponse.class));
    }

    @Override
    public ResponseEntity<List<VehicleResponse>> findAll() {
        List<VehicleEntity> entyties = vehicleService.findAll();
        return ResponseEntity.ok(toCollection(entyties, VehicleResponse.class));
    }

    @Override
    public ResponseEntity<VehicleResponse> findById(Long id) {
        VehicleEntity entity = vehicleService.findById(id);
        return ResponseEntity.ok(toMapper(entity, VehicleResponse.class));
    }

    @Override
    public void delete(Long id) {
        VehicleEntity entity = vehicleService.findById(id);
        vehicleService.deleteById(entity.getId());
    }

    @Override
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
    public ResponseEntity<VehicleResponse> findByLicensePlate(String licensePlate) {
        VehicleEntity entity = vehicleService.findByLicensePlate(licensePlate);
        return ResponseEntity.ok(toMapper(entity, VehicleResponse.class));
    }

    @Override
    public ResponseEntity<VehicleResponse> findByRenavam(String renavam) {
        VehicleEntity entity = vehicleService.findByRenavam(renavam);
        return ResponseEntity.ok(toMapper(entity, VehicleResponse.class));
    }
}
