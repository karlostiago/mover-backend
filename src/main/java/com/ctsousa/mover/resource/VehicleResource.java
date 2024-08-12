package com.ctsousa.mover.resource;

import com.ctsousa.mover.core.api.VehicleApi;
import com.ctsousa.mover.mapper.VehicleMapper;
import com.ctsousa.mover.request.VehicleRequest;
import com.ctsousa.mover.response.VehicleResponse;
import com.ctsousa.mover.service.VehicleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/vechicles")
public class VehicleResource implements VehicleApi {

    private final VehicleService vehicleService;

    private final VehicleMapper vehicleMapper;

    public VehicleResource(VehicleService vehicleService, VehicleMapper vehicleMapper) {
        this.vehicleService = vehicleService;
        this.vehicleMapper = vehicleMapper;
    }

    @Override
    public ResponseEntity<VehicleResponse> add(VehicleRequest requestBody) {
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
