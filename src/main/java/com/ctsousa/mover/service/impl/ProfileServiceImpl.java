package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.PermissionEntity;
import com.ctsousa.mover.core.entity.ProfileEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.exception.severity.Severity;
import com.ctsousa.mover.core.service.impl.BaseServiceImpl;
import com.ctsousa.mover.enumeration.Functionality;
import com.ctsousa.mover.repository.PermissionRepository;
import com.ctsousa.mover.repository.ProfileRepository;
import com.ctsousa.mover.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public class ProfileServiceImpl extends BaseServiceImpl<ProfileEntity, Long> implements ProfileService {

    @Autowired
    private ProfileRepository repository;

    private final PermissionRepository permissionRepository;

    public ProfileServiceImpl(JpaRepository<ProfileEntity, Long> repository, PermissionRepository permissionRepository) {
        super(repository);
        this.permissionRepository = permissionRepository;
    }


    @Override
    public ProfileEntity save(ProfileEntity entity) {
        for (PermissionEntity permissionEntity : entity.getPermissions()) {
            PermissionEntity permission = permissionRepository.findByName(permissionEntity.getName());
            permissionEntity.setId(permission.getId());
        }
        return super.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        try {
            super.deleteById(id);
        } catch (Exception e) {
            throw new NotificationException("Esse perfil já esta em uso e não pode ser excluído.", Severity.ERROR);
        }
    }
}
