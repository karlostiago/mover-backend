package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.BrandEntity;
import com.ctsousa.mover.core.entity.ModelEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.exception.severity.Severity;
import com.ctsousa.mover.core.service.impl.AbstractServiceImpl;
import com.ctsousa.mover.repository.BrandRepository;
import com.ctsousa.mover.repository.ModelRepository;
import com.ctsousa.mover.service.ModelService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ModelServiceImpl extends AbstractServiceImpl<ModelEntity, Long> implements ModelService {

    private final ModelRepository modelRepository;

    private final BrandRepository brandRepository;

    public ModelServiceImpl(JpaRepository<ModelEntity, Long> repository, BrandRepository brandRepository) {
        super(repository);
        this.modelRepository = (ModelRepository) repository;
        this.brandRepository = brandRepository;
    }

    @Override
    public ModelEntity save(ModelEntity entity) {
        BrandEntity brandEntity = brandRepository.findById(entity.getBrand().getId())
                .orElseThrow(() -> new NotificationException("Marca não encontrada ao realizar o cadastro de modelo."));
        entity.setBrand(brandEntity);

        if (entity.isNew()) {
            if (modelRepository.existsByNameAndBrandName(entity.getName(), entity.getBrand().getName())) {
                throw new NotificationException("Já existe um modelo, com os dados informados.", Severity.WARNING);
            }
        } else if (!entity.isNew()) {
            if (modelRepository.existsByNameAndBrandNameNotId(entity.getName(), entity.getBrand().getName(), entity.getId())) {
                throw new NotificationException("Não foi possível atualizar, pois já existe um modelo, com os dados informados.", Severity.WARNING);
            }
        }

        return super.save(entity);
    }

    @Override
    public List<ModelEntity> findBy(String paramFilter) {
        if (paramFilter == null || paramFilter.trim().isEmpty()) return modelRepository.findAll();

        return modelRepository.findBy("%" + paramFilter.toUpperCase() + "%");
    }
}
