package com.ctsousa.mover.resource;

import com.ctsousa.mover.core.api.BrandApi;
import com.ctsousa.mover.domain.Brand;
import com.ctsousa.mover.core.entity.BrandEntity;
import com.ctsousa.mover.mapper.BrandMapper;
import com.ctsousa.mover.service.BrandService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<Brand> add(Brand brand) {
        Optional<BrandEntity> branchOpt = brandService.save(brandMapper.toEntity(brand));
        return ResponseEntity.ok(brandMapper.toDomain(branchOpt.get()));
    }

    @Override
    public ResponseEntity<List<Brand>> findAll() {
        return ResponseEntity.ok(brandMapper.toDomains(brandService.findAll()));
    }

    @Override
    public void delete(Long id) {
        brandService.deleteById(id);
    }

    @Override
    public ResponseEntity<Brand> findById(Long id) {
        return ResponseEntity.ok(brandMapper.toDomain(brandService.findById(id)));
    }

    @Override
    public ResponseEntity<Brand> update(Long id, Brand requestBody) {
        brandService.findById(id);
        requestBody.setId(id);
        var brandEntity = brandService.update(brandMapper.toEntity(requestBody));
        return ResponseEntity.ok(brandMapper.toDomain(brandEntity));
    }
}
