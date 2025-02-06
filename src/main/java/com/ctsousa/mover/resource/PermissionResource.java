package com.ctsousa.mover.resource;

import com.ctsousa.mover.core.api.PermissionApi;
import com.ctsousa.mover.core.api.resource.BaseResource;
import com.ctsousa.mover.core.entity.PermissionEntity;
import com.ctsousa.mover.domain.Permission;
import com.ctsousa.mover.enumeration.Functionality;
import com.ctsousa.mover.enumeration.PermissionType;
import com.ctsousa.mover.request.PermissionRequest;
import com.ctsousa.mover.response.FuncionalityResponse;
import com.ctsousa.mover.response.PermissionResponse;
import com.ctsousa.mover.response.PermissionTypeResponse;
import com.ctsousa.mover.scheduler.ModelScheduler;
import com.ctsousa.mover.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.ctsousa.mover.core.mapper.Transform.toCollection;
import static com.ctsousa.mover.core.mapper.Transform.toMapper;

@RestController
@RequestMapping("/permissions")
public class PermissionResource extends BaseResource<PermissionResponse, PermissionRequest, PermissionEntity> implements PermissionApi {

    @Autowired
    private PermissionService permissionService;


    public PermissionResource(PermissionService permissionService) {
        super(permissionService);
    }

    @Override
    public ResponseEntity<PermissionResponse> add(PermissionRequest request) {
        Permission permission = toMapper(request, Permission.class);
        PermissionEntity entity = permissionService.save(permission.toEntity());
        ModelScheduler.buffers.add(entity.getName());
        return ResponseEntity.ok(toMapper(entity, PermissionResponse.class));
    }

    @Override
    public ResponseEntity<PermissionResponse> update(Long id, PermissionRequest request) {
        permissionService.existsById(id);
        Permission permission = toMapper(request, Permission.class);
        PermissionEntity entity = permission.toEntity();
        permissionService.save(entity);
        return ResponseEntity.ok(toMapper(entity, PermissionResponse.class));
    }

    @Override
    public ResponseEntity<List<FuncionalityResponse>> findAllFeatures() {
        List<Functionality> features = List.of(Functionality.values());
        List<FuncionalityResponse> response = features.stream().map(feature -> {
            FuncionalityResponse funcionalityResponse = new FuncionalityResponse();
            funcionalityResponse.setId(feature.getCode());
            funcionalityResponse.setName(feature.getDescription().toUpperCase());
            funcionalityResponse.setCodeMenu(feature.getMenu().getCode());
            funcionalityResponse.setMenuName(feature.getMenu().getDescription().toUpperCase());
            funcionalityResponse.setActive(false);
            return funcionalityResponse;
        }).toList();
        return ResponseEntity.ok(toCollection(response, FuncionalityResponse.class));
    }

    @Override
    public ResponseEntity<List<PermissionTypeResponse>> findAllPermissionTypes() {
        List<PermissionType> permissionTypes = List.of(PermissionType.values());
        return ResponseEntity.ok(toCollection(permissionTypes, PermissionTypeResponse.class));
    }

    @Override
    public Class<?> responseClass() {
        return PermissionResponse.class;
    }
}
