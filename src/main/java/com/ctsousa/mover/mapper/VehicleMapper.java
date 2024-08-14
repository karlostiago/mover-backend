package com.ctsousa.mover.mapper;

import com.ctsousa.mover.core.entity.VehicleEntity;
import com.ctsousa.mover.core.mapper.MapperToDomain;
import com.ctsousa.mover.core.mapper.MapperToResponse;
import com.ctsousa.mover.domain.Vehicle;
import com.ctsousa.mover.request.VehicleRequest;
import com.ctsousa.mover.response.VehicleResponse;

import java.util.List;

public class VehicleMapper implements MapperToDomain<Vehicle, VehicleRequest>, MapperToResponse<VehicleResponse, VehicleEntity> {

    @Override
    public Vehicle toDomain(VehicleRequest request) {
        return null;
    }

    @Override
    public VehicleResponse toResponse(VehicleEntity entity) {
        return null;
    }

    @Override
    public List<VehicleResponse> toCollections(List<VehicleEntity> list) {
        return List.of();
    }
}
