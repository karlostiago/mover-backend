package com.ctsousa.mover.resource;

import com.ctsousa.mover.core.api.ModelApi;
import com.ctsousa.mover.core.entity.ModelEntity;
import com.ctsousa.mover.domain.Model;
import com.ctsousa.mover.request.ModelRequest;
import com.ctsousa.mover.response.ModelResponse;
import com.ctsousa.mover.service.ModelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.ctsousa.mover.core.mapper.Transform.toCollection;
import static com.ctsousa.mover.core.mapper.Transform.toMapper;

@RestController
@RequestMapping("/models")
public class ModelResource implements ModelApi {

    private final ModelService modelService;

    public ModelResource(ModelService modelService) {
        this.modelService = modelService;
    }

    @Override
    public ResponseEntity<ModelResponse> add(ModelRequest request) {
        Model model = toMapper(request, Model.class);
        ModelEntity entity = modelService.save(model.toEntity());
        return ResponseEntity.ok(toMapper(entity, ModelResponse.class));
    }

    @Override
    public ResponseEntity<List<ModelResponse>> findAll() {
        return ResponseEntity.ok(toCollection(modelService.findAll(), ModelResponse.class));
    }

    @Override
    public ResponseEntity<ModelResponse> findById(Long id) {
        ModelEntity entity = modelService.findById(id);
        return ResponseEntity.ok(toMapper(entity, ModelResponse.class));
    }

    @Override
    public void delete(Long id) {
        ModelEntity entity = modelService.findById(id);
        modelService.deleteById(entity.getId());
    }

    @Override
    public ResponseEntity<ModelResponse> update(Long id, ModelRequest requestBody) {
        modelService.existsById(id);
        Model domain = toMapper(requestBody, Model.class);
        ModelEntity entity = domain.toEntity();
        modelService.save(entity);
        return ResponseEntity.ok(toMapper(entity, ModelResponse.class));
    }

    @Override
    public ResponseEntity<List<ModelResponse>> filterBy(String search) {
        List<ModelEntity> entities = modelService.findBy(search);
        return ResponseEntity.ok(toCollection(entities, ModelResponse.class));
    }

    @Override
    public ResponseEntity<List<ModelResponse>> findByBrandId(Long brandId) {
        List<ModelEntity> entities = modelService.findByBrandId(brandId);
        return ResponseEntity.ok(toCollection(entities, ModelResponse.class));
    }
}
