package com.ctsousa.mover.service;

import com.ctsousa.mover.core.entity.BrandEntity;
import com.ctsousa.mover.core.entity.ModelEntity;
import com.ctsousa.mover.core.entity.SymbolEntity;
import com.ctsousa.mover.repository.BrandRepository;
import com.ctsousa.mover.repository.ModelRepository;
import com.ctsousa.mover.repository.SymbolRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class ModelServiceTest {

    @Autowired
    private ModelService modelService;

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private SymbolRepository symbolRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        savedSymbol();
        savedBrand();
    }

    @Test
    void shouldSavedModel() {
        ModelEntity entity = new ModelEntity();
        entity.setName("FASTBACK");
        entity.setBrand(brandRepository.findAll().get(0));
        entity.setYearModel(2025);
        entity.setYearManufacture(2025);
        entity.setActive(true);
        entity.setColor("BRANCO");

        ModelEntity savedEntity = modelService.save(entity);
        Assertions.assertNotNull(savedEntity.getId());
    }

    @Test
    void shouldUpdatedModel() {
        ModelEntity entity = modelService.findById(1L);
        entity.setColor("CINZA");
        entity.setYearModel(2023);
        entity.setYearManufacture(2023);

        ModelEntity updatedEntity = modelService.update(entity);

        Assertions.assertEquals("CINZA", updatedEntity.getColor());
        Assertions.assertEquals(2023, updatedEntity.getYearModel());
        Assertions.assertEquals(2023, updatedEntity.getYearManufacture());
    }

    private void savedSymbol() {
        SymbolEntity entity = new SymbolEntity();
        entity.setDescription("FIAT");
        entity.setImageBase64("da32s1e6q4651===");
        entity.setActive(true);
        symbolRepository.save(entity);
    }

    private void savedBrand() {
        BrandEntity entity = new BrandEntity();
        entity.setName("FIAT");
        entity.setActive(true);
        entity.setSymbol(symbolRepository.findAll().get(0));
        brandRepository.save(entity);
    }
}
