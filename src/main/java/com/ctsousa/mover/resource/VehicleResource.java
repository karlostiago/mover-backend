package com.ctsousa.mover.resource;

import com.ctsousa.mover.core.api.VehicleApi;
import com.ctsousa.mover.core.entity.BrandEntity;
import com.ctsousa.mover.core.entity.ModelEntity;
import com.ctsousa.mover.core.entity.VehicleEntity;
import com.ctsousa.mover.core.mapper.Transform;
import com.ctsousa.mover.domain.Vehicle;
import com.ctsousa.mover.request.VehicleRequest;
import com.ctsousa.mover.response.VehicleResponse;
import com.ctsousa.mover.service.BrandService;
import com.ctsousa.mover.service.ModelService;
import com.ctsousa.mover.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
        Vehicle domain = Transform.toMapper(request, Vehicle.class);
        VehicleEntity entity = domain.toEntity();
        entity.setBrand(brandService.findById(domain.getBrand().getId()));
        entity.setModel(modelService.findById(domain.getModel().getId()));
        entity = vehicleService.save(entity);
        return null;
    }

    @Override
    public ResponseEntity<List<VehicleResponse>> findAll() {
        return null;
    }

    @Override
    public ResponseEntity<VehicleResponse> findById(Long id) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public ResponseEntity<VehicleResponse> update(Long id, VehicleRequest requestBody) {
        return null;
    }
}
