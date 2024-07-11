package com.ctsousa.mover.resource;

import com.ctsousa.mover.core.api.BrandApi;
import com.ctsousa.mover.core.entity.BrandEntity;
import com.ctsousa.mover.domain.Brand;
import com.ctsousa.mover.mapper.BrandMapper;
import com.ctsousa.mover.request.BrandRequest;
import com.ctsousa.mover.response.BrandResponse;
import com.ctsousa.mover.service.BrandService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/brands")
public class BrandResource implements BrandApi {

    private final BrandService brandService;

    private final BrandMapper brandMapper;

    public BrandResource(BrandService brandService, BrandMapper brandMapper) {
        this.brandService = brandService;
        this.brandMapper = brandMapper;
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
        brandService.deleteById(id);
    }

    @Override
    public ResponseEntity<BrandResponse> update(Long id, BrandRequest request) {
        return null;
    }
}
