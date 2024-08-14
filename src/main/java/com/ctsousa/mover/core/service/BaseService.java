package com.ctsousa.mover.core.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BaseService<T, ID> {

    T save(T entity);

    void deleteById(ID id);

    T findById(ID id);

    List<T> findAll();

    T update(T entity);

    void existsById(ID id);
}
