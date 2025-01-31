package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.PermissionEntity;
import com.ctsousa.mover.core.service.impl.BaseServiceImpl;
import com.ctsousa.mover.repository.PermissionRepository;
import com.ctsousa.mover.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PermissionServiceImpl extends BaseServiceImpl<PermissionEntity, Long> implements PermissionService {

    @Autowired
    private PermissionRepository repository;

    public PermissionServiceImpl(PermissionRepository repository) {
        super(repository);
    }
}
