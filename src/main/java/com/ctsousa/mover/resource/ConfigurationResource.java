package com.ctsousa.mover.resource;

import com.ctsousa.mover.core.api.ConfigurationApi;
import com.ctsousa.mover.core.api.resource.BaseResource;
import com.ctsousa.mover.core.entity.ConfigurationEntity;
import com.ctsousa.mover.domain.Configuration;
import com.ctsousa.mover.enumeration.TypeValueConfiguration;
import com.ctsousa.mover.request.ConfigurationRequest;
import com.ctsousa.mover.response.ConfigurationResponse;
import com.ctsousa.mover.response.TypeConfigurationResponse;
import com.ctsousa.mover.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static com.ctsousa.mover.core.mapper.Transform.toCollection;
import static com.ctsousa.mover.core.mapper.Transform.toMapper;

@RestController
@RequestMapping("/configurations")
public class ConfigurationResource extends BaseResource<ConfigurationResponse, ConfigurationRequest, ConfigurationEntity> implements ConfigurationApi {

    @Autowired
    private ConfigurationService configurationService;

    public ConfigurationResource(ConfigurationService configurationService) {
        super(configurationService);
    }

    @Override
    public ResponseEntity<ConfigurationResponse> add(ConfigurationRequest request) {
        Configuration configuration = toMapper(request, Configuration.class);
        ConfigurationEntity entity = configurationService.save(configuration.toEntity());
        return ResponseEntity.ok(toMapper(entity, ConfigurationResponse.class));
    }

    @Override
    public ResponseEntity<ConfigurationResponse> update(Long id, ConfigurationRequest request) {
        configurationService.existsById(id);
        Configuration configuration = toMapper(request, Configuration.class);
        ConfigurationEntity entity = configuration.toEntity();
        configurationService.save(entity);
        return ResponseEntity.ok(toMapper(entity, ConfigurationResponse.class));
    }

    @Override
    public ResponseEntity<List<TypeConfigurationResponse>> findAllTypes() {
        List<TypeValueConfiguration> types = Stream.of(TypeValueConfiguration.values())
                .sorted(Comparator.comparing(TypeValueConfiguration::getCode))
                .toList();
        return ResponseEntity.ok(toCollection(types, TypeConfigurationResponse.class));
    }

    @Override
    public ResponseEntity<List<ConfigurationResponse>> filterBy(String search) {
        List<ConfigurationEntity> entities = configurationService.filterBy(search);
        return ResponseEntity.ok(toCollection(entities, ConfigurationResponse.class));
    }

    @Override
    public Class<?> responseClass() {
        return ConfigurationResponse.class;
    }
}
