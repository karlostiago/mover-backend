package com.ctsousa.mover.core.api.resource;

import com.ctsousa.mover.core.api.Api;
import com.ctsousa.mover.core.service.BaseService;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.ctsousa.mover.core.mapper.Transform.toCollection;
import static com.ctsousa.mover.core.mapper.Transform.toMapper;

public abstract class BaseResource<RESPONSE, REQUEST, T> implements Api<REQUEST, RESPONSE> {

    private final BaseService<T, Long> service;

    public  BaseResource(BaseService<T, Long> service) {
        this.service = service;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ResponseEntity<List<RESPONSE>> findAll() {
        List<T> entities = service.findAll();
        List<RESPONSE> response = (List<RESPONSE>) toCollection(entities, responseClass());
        return ResponseEntity.ok(response);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ResponseEntity<RESPONSE> findById(Long id) {
        T entity = service.findById(id);
        RESPONSE response = (RESPONSE) toMapper(entity, responseClass());
        return ResponseEntity.ok(response);
    }

    @Override
    public void delete(Long id) {
        service.existsById(id);
        service.deleteById(id);
    }

    public abstract Class<?> responseClass();
}
