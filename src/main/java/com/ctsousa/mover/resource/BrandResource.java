package com.ctsousa.mover.resource;

import com.ctsousa.mover.core.api.resource.BaseResource;
import com.ctsousa.mover.core.api.BrandApi;
import com.ctsousa.mover.core.entity.BrandEntity;
import com.ctsousa.mover.core.entity.SymbolEntity;
import com.ctsousa.mover.domain.Brand;
import com.ctsousa.mover.domain.Symbol;
import com.ctsousa.mover.request.BrandRequest;
import com.ctsousa.mover.response.BrandResponse;
import com.ctsousa.mover.service.BrandService;
import com.ctsousa.mover.service.SymbolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.util.List;

import static com.ctsousa.mover.core.mapper.Transform.toCollection;
import static com.ctsousa.mover.core.mapper.Transform.toMapper;

@RestController
@RequestMapping("/brands")
public class BrandResource extends BaseResource<BrandResponse, BrandRequest, BrandEntity> implements BrandApi {

    @Autowired
    private BrandService brandService;

    private final SymbolService symbolService;

    public BrandResource(BrandService brandService, SymbolService symbolService) {
        super(brandService);
        this.symbolService = symbolService;
    }

    @Override
    public ResponseEntity<List<BrandResponse>> filterByName(String name) {
        List<BrandEntity> entities = brandService.filterByName(name);
        return ResponseEntity.ok(toCollection(entities, BrandResponse.class));
    }

    @Override
    public ResponseEntity<BrandResponse> add(BrandRequest request) {
        Brand brand = toMapper(request, Brand.class);
        BrandEntity entity = brandService.save(brand.toEntity());
        return ResponseEntity.ok(toMapper(entity, BrandResponse.class));
    }

    @Override
    public ResponseEntity<BrandResponse> update(Long id, BrandRequest request) {
        brandService.existsById(id);
        Brand domain = toMapper(request, Brand.class);
        BrandEntity entity = domain.toEntity();
        brandService.save(entity);
        return ResponseEntity.ok(toMapper(entity, BrandResponse.class));
    }

    @Override
    public ResponseEntity<Void> upload(MultipartFile file, String filename) {
        BufferedImage image = brandService.upload(file);
        String imageBase64 = brandService.toBase64(image);
        Symbol symbol = new Symbol(filename, imageBase64);
        SymbolEntity entity = symbol.toEntity();
        symbolService.save(entity);
        return ResponseEntity.ok().build();
    }

    @Override
    public Class<?> responseClass() {
        return BrandResponse.class;
    }
}
