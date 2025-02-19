package com.ctsousa.mover.resource;

import com.ctsousa.mover.core.api.ConfigurationApi;
import com.ctsousa.mover.core.api.resource.BaseResource;
import com.ctsousa.mover.core.entity.ParameterEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.security.Security;
import com.ctsousa.mover.domain.Parameter;
import com.ctsousa.mover.enumeration.TypeValueParameter;
import com.ctsousa.mover.request.ParameterRequest;
import com.ctsousa.mover.response.ParameterResponse;
import com.ctsousa.mover.response.TypeParameterResponse;
import com.ctsousa.mover.service.ParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static com.ctsousa.mover.core.mapper.Transform.toCollection;
import static com.ctsousa.mover.core.mapper.Transform.toMapper;

@RestController
@RequestMapping("/parameters")
public class ParameterResource extends BaseResource<ParameterResponse, ParameterRequest, ParameterEntity> implements ConfigurationApi {

    @Autowired
    private ParameterService parameterService;

    public ParameterResource(ParameterService parameterService) {
        super(parameterService);
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Parameter.REGISTER_PARAMETERS)
    public ResponseEntity<ParameterResponse> add(ParameterRequest request) {
        Parameter domain = toMapper(request, Parameter.class);
        ParameterEntity entity = parameterService.save(domain.toEntity());
        return ResponseEntity.ok(toMapper(entity, ParameterResponse.class));
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Parameter.UPDATE_PARAMETERS)
    public ResponseEntity<ParameterResponse> update(Long id, ParameterRequest request) {
        ParameterEntity entity = parameterService.findById(id);
        Parameter domain = toMapper(request, Parameter.class);
        boolean verifiedKey = parameterService.verifyKeySystem(entity.getKey());
        if (verifiedKey) {
            if (!domain.getKey().equalsIgnoreCase(entity.getKey())) {
                throw new NotificationException("Essa Parametrização é essencial para o sistema e não pode ser atualizada.");
            }
        }
        parameterService.save(domain.toEntity());
        return ResponseEntity.ok(toMapper(entity, ParameterResponse.class));
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Parameter.DELETE_PARAMETERS)
    public void delete(Long id) {
        parameterService.existsById(id);
        super.delete(id);
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Parameter.FILTER_PARAMETERS)
    public ResponseEntity<List<TypeParameterResponse>> findAllTypes() {
        List<TypeValueParameter> types = Stream.of(TypeValueParameter.values())
                .sorted(Comparator.comparing(TypeValueParameter::getCode))
                .toList();
        return ResponseEntity.ok(toCollection(types, TypeParameterResponse.class));
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Parameter.FILTER_PARAMETERS)
    public ResponseEntity<List<ParameterResponse>> filterBy(String search) {
        List<ParameterEntity> entities = parameterService.filterBy(search);
        return ResponseEntity.ok(toCollection(entities, ParameterResponse.class));
    }

    @Override
    @PreAuthorize(Security.PreAutorize.Parameter.FILTER_PARAMETERS)
    public ResponseEntity<List<ParameterResponse>> findAll() {
        return super.findAll();
    }

    @Override
    public Class<?> responseClass() {
        return ParameterResponse.class;
    }
}
