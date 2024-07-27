package com.ctsousa.mover.resource;

import com.ctsousa.mover.core.api.BrandApi;
import com.ctsousa.mover.core.entity.BrandEntity;
import com.ctsousa.mover.core.entity.SymbolEntity;
import com.ctsousa.mover.domain.Brand;
import com.ctsousa.mover.domain.Symbol;
import com.ctsousa.mover.mapper.BrandMapper;
import com.ctsousa.mover.request.BrandRequest;
import com.ctsousa.mover.response.BrandResponse;
import com.ctsousa.mover.service.BrandService;
import com.ctsousa.mover.service.SymbolService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.util.List;

@RestController
@RequestMapping("/brands")
public class BrandResource implements BrandApi {

    private final BrandService brandService;

    private final BrandMapper brandMapper;

    private final SymbolService symbolService;

    public BrandResource(BrandService brandService, BrandMapper brandMapper, SymbolService symbolService) {
        this.brandService = brandService;
        this.brandMapper = brandMapper;
        this.symbolService = symbolService;
    }

    @Override
    public ResponseEntity<List<BrandResponse>> filterByName(String name) {
        List<BrandEntity> entities = brandService.filterByName(name);
        return ResponseEntity.ok(brandMapper.toCollections(entities));
    }

    @Override
    public ResponseEntity<BrandResponse> add(BrandRequest request) {
        Brand brand = brandMapper.toDomain(request);
        BrandEntity entity = brandService.save(brand.toEntity());
        BrandResponse response = brandMapper.toResponse(entity);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<List<BrandResponse>> findAll() {
        List<BrandEntity> entities = brandService.findAll();
        return ResponseEntity.ok(brandMapper.toCollections(entities));
    }

    @Override
    public ResponseEntity<BrandResponse> findById(Long id) {
        BrandEntity entity = brandService.findById(id);
        return ResponseEntity.ok(brandMapper.toResponse(entity));
    }

    @Override
    public void delete(Long id) {
        BrandEntity entity = brandService.findById(id);
        brandService.deleteById(id);
        symbolService.deleteById(entity.getSymbol().getId());
    }

    @Override
    public ResponseEntity<BrandResponse> update(Long id, BrandRequest request) {
        brandService.findById(id);
        Brand domain = brandMapper.toDomain(request);
        BrandEntity entity = domain.toEntity();
        entity.setId(id);
        brandService.save(entity);
        return ResponseEntity.ok(brandMapper.toResponse(entity));
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
}
