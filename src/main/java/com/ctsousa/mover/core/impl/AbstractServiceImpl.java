package com.ctsousa.mover.core.impl;

import com.ctsousa.mover.core.service.AbstractService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public abstract class AbstractServiceImpl<T, ID> implements AbstractService<T, ID> {

    protected JpaRepository<T, ID> repository;

    public AbstractServiceImpl(JpaRepository<T, ID> repository) {
        this.repository = repository;
    }

    @Override
    public Optional<T> add(T entity) {
        return Optional.of(repository.save(entity));
    }

    @Override
    public void deleteById(ID id) {
        findById(id);
        repository.deleteById(id);
    }

    @Override
    public T findById(ID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Id solicitado nao encontrado."));
    }

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }
}
