package com.ctsousa.mover.service;

import com.ctsousa.mover.core.entity.SubCategoryEntity;
import com.ctsousa.mover.core.service.BaseService;

import java.util.List;

public interface SubCategoryService extends BaseService<SubCategoryEntity, Long> {

    List<SubCategoryEntity> filterBy(String search);
}
