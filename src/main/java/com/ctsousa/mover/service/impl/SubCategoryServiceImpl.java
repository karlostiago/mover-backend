package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.SubCategoryEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.exception.severity.Severity;
import com.ctsousa.mover.core.service.impl.BaseServiceImpl;
import com.ctsousa.mover.repository.SubCategoryRepository;
import com.ctsousa.mover.service.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SubCategoryServiceImpl extends BaseServiceImpl<SubCategoryEntity, Long> implements SubCategoryService {

    @Autowired
    private SubCategoryRepository repository;

    public SubCategoryServiceImpl(SubCategoryRepository repository) {
        super(repository);
    }

    @Override
    public SubCategoryEntity save(SubCategoryEntity entity) {
        if (entity.isNew()) {
            if (repository.existsByDescription(entity.getDescription())) {
                throw new NotificationException("Já existe uma subcategoria cadastrado com os dados informados.", Severity.WARNING);
            }
        } else if (!entity.isNew()) {
            if (repository.existsByDescriptionNotId(entity.getDescription(), entity.getId())) {
                throw new NotificationException("Não foi possível atualizar, essa subcategoria já.", Severity.WARNING);
            }
        }

        return super.save(entity);
    }


    @Override
    public List<SubCategoryEntity> filterBy(String search) {
        if (search == null || search.isEmpty()) return repository.findAll();
        return repository.findBy(search);
    }
}
