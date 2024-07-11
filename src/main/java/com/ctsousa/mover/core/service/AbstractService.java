package com.ctsousa.mover.core.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface AbstractService<T, ID> {

    T save(T entity);

    void deleteById(ID id);

    T findById(ID id);

    List<T> findAll();

    T update(T entity);
}
