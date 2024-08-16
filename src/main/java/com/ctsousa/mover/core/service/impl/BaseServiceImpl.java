package com.ctsousa.mover.core.service.impl;

import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.service.BaseService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public abstract class BaseServiceImpl<T, ID> implements BaseService<T, ID> {

    protected JpaRepository<T, ID> repository;

    public BaseServiceImpl(JpaRepository<T, ID> repository) {
        this.repository = repository;
    }

    @Override
    public T save(T entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteById(ID id) {
        findById(id);
        repository.deleteById(id);
    }

    @Override
    public T findById(ID id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotificationException("Id solicitado nao encontrado."));
    }

    @Override
    public List<T> findAll() {
        return repository.findAll();
    }

    @Override
    public T update(T entity) {
        return repository.save(entity);
    }

    @Override
    public void existsById(ID id) {
        findById(id);
    }
}
