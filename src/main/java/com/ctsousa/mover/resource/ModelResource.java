package com.ctsousa.mover.resource;

import com.ctsousa.mover.core.api.ModelApi;
import com.ctsousa.mover.core.entity.ModelEntity;
import com.ctsousa.mover.domain.Model;
import com.ctsousa.mover.mapper.ModelMapper;
import com.ctsousa.mover.request.ModelRequest;
import com.ctsousa.mover.response.ModelResponse;
import com.ctsousa.mover.service.ModelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/models")
public class ModelResource implements ModelApi {

    private final ModelService modelService;

    private final ModelMapper modelMapper;

    public ModelResource(ModelService modelService, ModelMapper modelMapper) {
        this.modelService = modelService;
        this.modelMapper = modelMapper;
    }

    @Override
    public ResponseEntity<ModelResponse> add(ModelRequest requestBody) {
        Model model = modelMapper.toDomain(requestBody);
        ModelEntity entity = modelService.save(model.toEntity());
        ModelResponse response = modelMapper.toResponse(entity);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<List<ModelResponse>> findAll() {
        return ResponseEntity.ok(modelMapper.toCollections(modelService.findAll()));
    }

    @Override
    public ResponseEntity<ModelResponse> findById(Long id) {
        ModelEntity entity = modelService.findById(id);
        return ResponseEntity.ok(modelMapper.toResponse(entity));
    }

    @Override
    public void delete(Long id) {
        ModelEntity entity = modelService.findById(id);
        modelService.deleteById(entity.getId());
    }

    @Override
    public ResponseEntity<ModelResponse> update(Long id, ModelRequest requestBody) {
        modelService.findById(id);
        Model domain = modelMapper.toDomain(requestBody);
        ModelEntity entity = domain.toEntity();
        entity.setId(id);
        modelService.save(entity);
        return ResponseEntity.ok(modelMapper.toResponse(entity));
    }

    @Override
    public ResponseEntity<List<ModelResponse>> filterBy(String search) {
        List<ModelEntity> entities = modelService.findBy(search);
        return ResponseEntity.ok(modelMapper.toCollections(entities));
    }
}
