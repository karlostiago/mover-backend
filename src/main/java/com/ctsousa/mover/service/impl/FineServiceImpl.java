package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.FineEntity;
import com.ctsousa.mover.core.service.impl.BaseServiceImpl;
import com.ctsousa.mover.repository.FineRepository;
import com.ctsousa.mover.service.FineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FineServiceImpl extends BaseServiceImpl<FineEntity, Long> implements FineService {

    @Autowired
    private FineRepository fineRepository;

    public FineServiceImpl(FineRepository fineRepository) {
        super(fineRepository);
    }

    @Override
    public FineEntity save(FineEntity entity) {
        return null;
    }
}
