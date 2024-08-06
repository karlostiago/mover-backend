package com.ctsousa.mover.service;

import com.ctsousa.mover.core.entity.BrandEntity;
import com.ctsousa.mover.core.entity.ModelEntity;
import com.ctsousa.mover.core.entity.SymbolEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.repository.BrandRepository;
import com.ctsousa.mover.repository.ModelRepository;
import com.ctsousa.mover.repository.SymbolRepository;
import org.junit.jupiter.api.*;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ModelServiceTest {

    @Autowired
    private ModelService modelService;

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private SymbolRepository symbolRepository;

    @BeforeAll
    void setup() {
        MockitoAnnotations.openMocks(this);
        savedSymbol();
        savedBrand();
    }

    @AfterAll
    void after() {
        modelRepository.deleteAll();
        brandRepository.deleteAll();
        symbolRepository.deleteAll();
    }

    @Test
    @Order(1)
    void shouldSavedModel() {
        ModelEntity savedEntity = modelService.save(getModelEntity());
        Assertions.assertNotNull(savedEntity.getId());
    }

    @Test
    @Order(2)
    void shouldUpdatedModel() {
        ModelEntity entity = modelService.findById(1L);
        entity.setColor("CINZA");
        entity.setYearModel(2023);
        entity.setYearManufacture(2023);

        ModelEntity updatedEntity = modelService.save(entity);

        Assertions.assertEquals("CINZA", updatedEntity.getColor());
        Assertions.assertEquals(2023, updatedEntity.getYearModel());
        Assertions.assertEquals(2023, updatedEntity.getYearManufacture());
    }

    @Test
    @Order(3)
    void shouldFilterById() {
        ModelEntity entity = modelService.findById(1L);
        Assertions.assertNotNull(entity.getId());
    }

    @Test
    @Order(4)
    void shouldFilteredAll() {
        List<ModelEntity> entities = modelService.findAll();
        Assertions.assertEquals(1, entities.size());
    }

    @Test
    @Order(5)
    void shouldReturnAllWhenFilterdWithoutParameter() {
        String params = ";;";
        List<ModelEntity> entities = modelService.findBy(params);
        Assertions.assertEquals(1, entities.size());

        params = ";null;";
        entities = modelService.findBy(params);
        Assertions.assertEquals(1, entities.size());
    }

    @Test
    @Order(6)
    void shouldFilteredByColorAndYearModel() {
        String params = "CINZA;;2023";
        List<ModelEntity> entities = modelService.findBy(params);
        Assertions.assertEquals(1, entities.size());
    }

    @Test
    @Order(7)
    void shouldFilteredByColorAndYearManufacture() {
        String params = "CINZA;2023;";
        List<ModelEntity> entities = modelService.findBy(params);
        Assertions.assertEquals(1, entities.size());
    }

    @Test
    @Order(8)
    void shouldFilteredByBrandNameAndYearManufacture() {
        String params = "FIAT;;2023";
        List<ModelEntity> entities = modelService.findBy(params);
        Assertions.assertEquals(1, entities.size());
    }

    @Test
    @Order(9)
    void shouldFilteredByBrandNameAndYearModel() {
        String params = "FIAT;2023;";
        List<ModelEntity> entities = modelService.findBy(params);
        Assertions.assertEquals(1, entities.size());
    }

    @Test
    @Order(9)
    void shouldFilteredByModelNameAndYearModel() {
        String params = "FASTBACK;2023;";
        List<ModelEntity> entities = modelService.findBy(params);
        Assertions.assertEquals(1, entities.size());
    }

    @Test
    @Order(10)
    void shouldFilteredByModelNameAndYearManufacture() {
        String params = "FASTBACK;;2023";
        List<ModelEntity> entities = modelService.findBy(params);
        Assertions.assertEquals(1, entities.size());
    }

    @Test
    @Order(11)
    void shouldFilteredByYearModelAndYearManufacture() {
        String params = ";2023;2023";
        List<ModelEntity> entities = modelService.findBy(params);
        Assertions.assertEquals(1, entities.size());
    }

    @Test
    @Order(12)
    void shouldFilteredByYearModel() {
        String params = ";;2023";
        List<ModelEntity> entities = modelService.findBy(params);
        Assertions.assertEquals(1, entities.size());
    }

    @Test
    @Order(13)
    void shouldFilteredByYearManufacture() {
        String params = ";2023;";
        List<ModelEntity> entities = modelService.findBy(params);
        Assertions.assertEquals(1, entities.size());
    }

    @Test
    @Order(14)
    void shouldFilteredByModelNameOrBrandNameOrColor() {
        String params = "FASTBACK";
        List<ModelEntity> entities = modelService.findBy(params);
        Assertions.assertEquals(1, entities.size());
    }

    @Test
    @Order(15)
    void shouldReturnErrorWhenExistsModel() {
        try {
            ModelEntity entity = getModelEntity();
            entity.setYearModel(2023);
            entity.setYearManufacture(2023);
            entity.setColor("CINZA");
            modelService.save(entity);
            Assertions.fail();
        } catch (NotificationException e) {
            Assertions.assertEquals("Já existe um modelo, com os dados informados.", e.getMessage());
        }
    }

    @Test
    @Order(16)
    void shouldReturnErrorWhenUpdated() {
        try {
            modelService.save(getModelEntity());
            ModelEntity filteredEntity = modelService.findById(1L);
            filteredEntity.setColor("BRANCO");
            filteredEntity.setYearModel(2025);
            filteredEntity.setYearManufacture(2025);
            modelService.save(filteredEntity);
        } catch (NotificationException e) {
            Assertions.assertEquals("Não foi possível atualizar, pois já existe um modelo, com os dados informados.", e.getMessage());
        }
    }

    @Test
    @Order(17)
    void shouldDeletedById() {
        modelService.deleteById(1L);
        modelService.deleteById(2L);
        List<ModelEntity> entities = modelService.findAll();
        Assertions.assertEquals(0, entities.size());
    }

    private ModelEntity getModelEntity() {
        ModelEntity entity = new ModelEntity();
        entity.setName("FASTBACK");
        entity.setBrand(brandRepository.findAll().get(0));
        entity.setYearModel(2025);
        entity.setYearManufacture(2025);
        entity.setActive(true);
        entity.setColor("BRANCO");
        return entity;
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
