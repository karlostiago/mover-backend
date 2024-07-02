package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.impl.AbstractServiceImpl;
import com.ctsousa.mover.infrastructure.entity.BrandEntity;
import com.ctsousa.mover.service.BrandService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public class BrandServiceImpl extends AbstractServiceImpl<BrandEntity, Long> implements BrandService {

    public BrandServiceImpl(JpaRepository<BrandEntity, Long> repository) {
        super(repository);
    }
}
