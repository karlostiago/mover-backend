package com.ctsousa.mover.service;

import com.ctsousa.mover.core.entity.CategoryEntity;
import com.ctsousa.mover.core.service.BaseService;

import java.util.List;

public interface CategoryService extends BaseService<CategoryEntity, Long> {

    List<CategoryEntity> filterBy(String search);
}
