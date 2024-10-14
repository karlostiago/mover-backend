package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.CategoryEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.exception.severity.Severity;
import com.ctsousa.mover.core.service.impl.BaseServiceImpl;
import com.ctsousa.mover.enumeration.TypeCategory;
import com.ctsousa.mover.repository.CategoryRepository;
import com.ctsousa.mover.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryServiceImpl extends BaseServiceImpl<CategoryEntity, Long> implements CategoryService {

    @Autowired
    private CategoryRepository repository;

    public CategoryServiceImpl(CategoryRepository repository) {
        super(repository);
    }

    @Override
    public CategoryEntity save(CategoryEntity entity) {
        if (entity.isNew()) {
            if (repository.existsByDescription(entity.getDescription())) {
                throw new NotificationException("Já existe uma categoria cadastrada com os dados informados.", Severity.WARNING);
            }
        } else if (!entity.isNew()) {
            if (repository.existsByDescriptionNotId(entity.getDescription(), entity.getId())) {
                throw new NotificationException("Não foi possível atualizar, já existe uma categoria cadastrada com os dados informados.", Severity.WARNING);
            }
        }

        return super.save(entity);
    }


    @Override
    public List<CategoryEntity> filterBy(String search) {
        if (search == null || search.isEmpty()) return repository.findAll();
        return repository.findBy(search);
    }

    @Override
    public List<CategoryEntity> filterBy(TypeCategory type) {
        return repository.findByType(type);
    }
}
