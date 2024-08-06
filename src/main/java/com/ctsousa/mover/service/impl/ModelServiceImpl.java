package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.BrandEntity;
import com.ctsousa.mover.core.entity.ModelEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.exception.severity.Severity;
import com.ctsousa.mover.core.service.impl.AbstractServiceImpl;
import com.ctsousa.mover.repository.BrandRepository;
import com.ctsousa.mover.repository.ModelRepository;
import com.ctsousa.mover.service.ModelService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
            if (modelRepository.existsByName(entity.getName(), entity.getYearManufacture(), entity.getYearModel(), entity.getColor(), entity.getBrand().getName())) {
                throw new NotificationException("Já existe um modelo, com os dados informados.", Severity.WARNING);
            }
        } else if (!entity.isNew()) {
            if (modelRepository.existsByNameNotId(entity.getName(), entity.getYearManufacture(), entity.getYearModel(), entity.getColor(), entity.getBrand().getName(), entity.getId())) {
                throw new NotificationException("Não foi possível atualizar, pois já existe um modelo, com os dados informados.", Severity.WARNING);
            }
        }

        return super.save(entity);
    }

    @Override
    public List<ModelEntity> findBy(String paramFilter) {
        String [] params = paramFilter.trim()
                .replaceAll("null", "")
                .split(";");

        if (params.length == 0) return modelRepository.findAll();

        String brandName = !params[0].isEmpty() ? params[0] : "";
        String modelName = !params[0].isEmpty() ? params[0] : "";
        String color = !params[0].isEmpty() ? params[0] : "";
        Integer yearManufacture = params.length > 1 && !params[1].isEmpty() ? Integer.valueOf(params[1]) : null;
        Integer yearModel = params.length > 2 && !params[2].isEmpty() ? Integer.valueOf(params[2]) : null;

        List<ModelEntity> entities = modelRepository.findByColorAndYearModel(yearModel, color);

        if(entities.isEmpty())
            entities = modelRepository.findByColorAndYearManufacture(yearManufacture, color);

        if(entities.isEmpty())
            entities = modelRepository.findByBrandNameAndYearModel(yearModel, brandName);

        if(entities.isEmpty())
            entities = modelRepository.findByBrandNameAndYearManufacture(yearManufacture, brandName);

        if(entities.isEmpty())
            entities = modelRepository.findByModelNameAndYearModel(yearModel, modelName);

        if(entities.isEmpty())
            entities = modelRepository.findByModelNameAndYearManufacture(yearManufacture, modelName);

        if(entities.isEmpty() && yearModel != null && yearManufacture != null)
            entities = modelRepository.findByYearManufactureAndYearModel(yearManufacture, yearModel);

        if(entities.isEmpty() && yearModel != null)
            entities = modelRepository.findByYearModel(yearModel);

        if(entities.isEmpty() && yearManufacture != null)
            entities = modelRepository.findByYearManufacture(yearManufacture);

        if(entities.isEmpty())
            entities = modelRepository.findBy(modelName, color, brandName);

        return entities;
    }
}
