package com.ctsousa.mover.core.mapper;

import com.ctsousa.mover.core.entity.BrandEntity;
import com.ctsousa.mover.core.entity.ModelEntity;
import com.ctsousa.mover.core.entity.SymbolEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.domain.Brand;
import com.ctsousa.mover.domain.Model;
import com.ctsousa.mover.domain.Symbol;
import com.ctsousa.mover.request.BrandRequest;
import com.ctsousa.mover.request.ModelRequest;
import com.ctsousa.mover.response.BrandResponse;
import com.ctsousa.mover.response.ModelResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TransformTest {

    @Test
    void shouldTransformToMapper() {
        SymbolEntity entity = new SymbolEntity("Symbol", "base64/da653126eq854we69s==");
        Symbol domain = Transform.toMapper(entity, Symbol.class);

        Assertions.assertEquals(entity.getDescription().toUpperCase(), domain.getDescription());
        Assertions.assertEquals(entity.getImageBase64(), domain.getImageBase64());
    }

//    @Test
//    void shouldIgnoreAttrWhenTransformToMapper() {
//        SymbolEntity entity = new SymbolEntity("Symbol", "base64/da653126eq854we69s==");
//
//        var thrown = Assertions.assertThrows(NotificationException.class, () -> Transform.toMapper(entity, Symbol.class, "imageBase64"));
//
//        Assertions.assertNotNull(thrown);
//        Assertions.assertNull(thrown.getMessage());
//    }

    @Test
    void shouldTransformToCollectionMapper() {
        List<SymbolEntity> entities = List.of(
            new SymbolEntity("Symbol1", "base64/da653126eq854we69s==1/"),
            new SymbolEntity("Symbol2", "base64/da653126eq854we69s==2/")
        );

        List<Symbol> domains = Transform.toCollection(entities, Symbol.class);

        Assertions.assertEquals(2, domains.size());
    }

    @Test
    void shouldTransformComplexClass() {
        BrandRequest request = new BrandRequest();
        request.setId(1L);
        request.setActive(true);
        request.setName("Brand");
        request.setSymbol(new Symbol("Symbol", "base64/da653126eq854we69s=="));

        Brand domain = Transform.toMapper(request, Brand.class);

        Assertions.assertEquals(request.getId(), domain.getId());
        Assertions.assertEquals(request.getName().toUpperCase(), domain.getName());
        Assertions.assertEquals(request.getActive(), domain.getActive());
        Assertions.assertEquals(request.getSymbol().getDescription(), domain.getSymbol().getDescription());
        Assertions.assertEquals(request.getSymbol().getImageBase64(), domain.getSymbol().getImageBase64());
    }

    @Test
    void shouldTransformCollectionComplexClass() {
        BrandEntity entity = new BrandEntity();
        entity.setId(1L);
        entity.setName("BRAND 1");
        entity.setSymbol(getSymbleEntity(1L, "Symbol 1", "base64/da653126eq854we69s==1/"));

        BrandEntity entity2 = new BrandEntity();
        entity2.setId(2L);
        entity2.setName("BRAND 2");
        entity2.setSymbol(getSymbleEntity(2L, "Symbol 2", "base64/da653126eq854we69s==2/"));

        BrandEntity entity3 = new BrandEntity();
        entity3.setId(3L);
        entity3.setName("BRAND 3");
        entity3.setSymbol(getSymbleEntity(3L, "Symbol 3", "base64/da653126eq854we69s==3/"));

        List<BrandResponse> response = Transform.toCollection(List.of(entity, entity2, entity3), BrandResponse.class);

        Assertions.assertEquals(3, response.size());
    }

    @Test
    void shouldTransformByAssociation() {
        ModelEntity entity = new ModelEntity();
        entity.setId(1L);
        entity.setName("MODEL 1");

        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setId(1L);
        brandEntity.setName("BRAND 1");
        brandEntity.setSymbol(getSymbleEntity(1L, "Symbol 1", "base64/da653126eq854we69s==1/"));
        entity.setBrand(brandEntity);

        ModelResponse response = Transform.toMapper(entity, ModelResponse.class);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(entity.getId(), response.getId());
        Assertions.assertEquals(entity.getName(), response.getName());
        Assertions.assertEquals(entity.getActive(), response.getActive());
        Assertions.assertEquals(brandEntity.getName(), response.getBrandName());
        Assertions.assertEquals(brandEntity.getId(), response.getBrandId());
    }

    @Test
    void shouldTransformByInvertAssociation() {
        ModelRequest request = new ModelRequest();
        request.setBrandId(1L);
        request.setName("ModelRequest");
        request.setActive(true);

        Model result = Transform.toMapper(request, Model.class);

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getBrand());
        Assertions.assertEquals(request.getBrandId(), result.getBrand().getId());
    }

    @Test
    void shouldTransformByComplexClassByComplexClass() {
        Model domain = new Model();
        domain.setName("Model");

        Brand brandDomain = new Brand();
        brandDomain.setId(1L);

        Symbol symbol = new Symbol("Symbol", "imageBase64");
        symbol.setId(1L);

        brandDomain.setSymbol(symbol);
        domain.setBrand(brandDomain);
        domain.setActive(true);

        var result = Transform.toMapper(domain, ModelEntity.class);

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getBrand());
        Assertions.assertEquals(domain.getBrand().getId(), result.getBrand().getId());
    }

    private SymbolEntity getSymbleEntity(Long id, String description, String imageBase64) {
        SymbolEntity entity = new SymbolEntity();
        entity.setId(id);
        entity.setDescription(description);
        entity.setImageBase64(imageBase64);
        return entity;
    }
}
