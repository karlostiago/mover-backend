package com.ctsousa.mover.core.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BaseService<T, ID> {

    T save(T entity);

    void deleteById(ID id);

    T findById(ID id);

    List<T> findAll();

    Page<T> findAll(Pageable pageable);

    T update(T entity);

    void existsById(ID id);
}
