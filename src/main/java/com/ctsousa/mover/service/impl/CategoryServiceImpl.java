package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.CategoryEntity;
import com.ctsousa.mover.core.entity.SubCategoryEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.exception.severity.Severity;
import com.ctsousa.mover.core.service.impl.BaseServiceImpl;
import com.ctsousa.mover.enumeration.TypeCategory;
import com.ctsousa.mover.repository.CategoryRepository;
import com.ctsousa.mover.repository.SubCategoryRepository;
import com.ctsousa.mover.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryServiceImpl extends BaseServiceImpl<CategoryEntity, Long> implements CategoryService {

    @Autowired
    private CategoryRepository repository;

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    public CategoryServiceImpl(CategoryRepository repository) {
        super(repository);
    }

    @Override
    public CategoryEntity save(CategoryEntity entity) {
        if (entity.isNew()) {
            if (repository.existsByDescriptionAndType(entity.getDescription(), entity.getType())) {
                throw new NotificationException("Já existe uma categoria cadastrada com os dados informados.", Severity.WARNING);
            }
        } else if (!entity.isNew()) {
            if (repository.existsByDescriptionNotId(entity.getDescription(), entity.getType(), entity.getId())) {
                throw new NotificationException("Não foi possível atualizar, já existe uma categoria cadastrada com os dados informados.", Severity.WARNING);
            }
            validateSubCategoryDeletion(entity);
        }

        return super.save(entity);
    }

    @Override
    public void deleteById(Long aLong) {
        try {
            super.deleteById(aLong);
        }
        catch (Exception e) {
            throw new NotificationException("Essa categoria já esta em uso e não pode ser excluída.", Severity.ERROR);
        }
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

    private void validateSubCategoryDeletion(CategoryEntity category) {
        List<SubCategoryEntity> subcategories = subCategoryRepository.findAllByCategory(category);
        List<SubCategoryEntity> subcategoriesInUse = subcategories.stream()
                .filter(s -> subCategoryRepository.inUse(s.getId()) > 0L)
                .toList();

        if (!subcategoriesInUse.isEmpty()) {
            String message = subcategoriesInUse.stream()
                    .map(SubCategoryEntity::getDescription)
                    .collect(Collectors.joining(", ", "As seguintes subcategorias não podem ser removidas, pois já estão em uso: ", "."));
            throw new NotificationException(message);
        }
    }
}
