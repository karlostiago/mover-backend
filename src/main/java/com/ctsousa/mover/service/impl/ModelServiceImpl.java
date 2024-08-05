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

        if (modelRepository.existsByName(entity.getName(), entity.getYearManufacture(), entity.getYearModel(), entity.getColor(), entity.getBrand().getName())) {
            throw new NotificationException("Já existe um modelo, com os dados informados.", Severity.WARNING);
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
        Integer yearModel = params.length > 2 && !params[2].isEmpty() ? Integer.valueOf(params[2]) : null;;
        return modelRepository.findBy(modelName, yearManufacture, yearModel, color, brandName);
    }
}