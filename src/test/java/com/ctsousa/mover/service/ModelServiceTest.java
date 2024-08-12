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
        entity.setActive(false);
        entity.setName("FASTBACK");

        ModelEntity updatedEntity = modelService.save(entity);

        Assertions.assertEquals("FASTBACK", updatedEntity.getName());
        Assertions.assertFalse(updatedEntity.getActive());
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
        String params = "";
        List<ModelEntity> entities = modelService.findBy(params);
        Assertions.assertEquals(1, entities.size());

        params = null;
        entities = modelService.findBy(params);
        Assertions.assertEquals(1, entities.size());
    }

    @Test
    @Order(6)
    void shouldReturnErrorWhenExistsModel() {
        try {
            ModelEntity entity = getModelEntity();
            entity.setName("FASTBACK");
            modelService.save(entity);
            Assertions.fail();
        } catch (NotificationException e) {
            Assertions.assertEquals("Já existe um modelo, com os dados informados.", e.getMessage());
        }
    }

    @Test
    @Order(7)
    void shouldReturnErrorWhenUpdated() {
        try {
            ModelEntity entity = getModelEntity();
            entity.setName("MOBI");
            modelService.save(entity);

            ModelEntity filteredEntity = modelService.findById(2L);
            filteredEntity.setName("FASTBACK");
            modelService.save(filteredEntity);
        } catch (NotificationException e) {
            Assertions.assertEquals("Não foi possível atualizar, pois já existe um modelo, com os dados informados.", e.getMessage());
        }
    }

    @Test
    @Order(8)
    void shouldFilterdByModelName() {
        String params = "MOBI";
        List<ModelEntity> entities = modelService.findBy(params);
        Assertions.assertEquals(1, entities.size());
    }

    @Test
    @Order(9)
    void shouldFilterdByBrandName() {
        String params = "FIAT";
        List<ModelEntity> entities = modelService.findBy(params);
        Assertions.assertEquals(2, entities.size());
    }

    @Test
    @Order(10)
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
        entity.setActive(true);
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
