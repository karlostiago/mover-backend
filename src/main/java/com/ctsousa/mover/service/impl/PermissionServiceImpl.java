package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.PermissionEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.service.impl.BaseServiceImpl;
import com.ctsousa.mover.enumeration.Functionality;
import com.ctsousa.mover.repository.PermissionRepository;
import com.ctsousa.mover.service.PermissionService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

import static com.ctsousa.mover.core.util.StringUtil.toUppercase;

@Component
public class PermissionServiceImpl extends BaseServiceImpl<PermissionEntity, Long> implements PermissionService {

    @Autowired
    private PermissionRepository repository;

    public PermissionServiceImpl(PermissionRepository repository) {
        super(repository);
    }

    @PostConstruct
    public void asyncWithDatabase() {
        List<PermissionEntity> permissions = removeExcessPermissions();

        List<String> features = permissions.stream()
                .map(PermissionEntity::getName)
                .toList();

        for (Functionality functionality : Functionality.values()) {
            if (!features.contains(functionality.name())) {
                repository.save(new PermissionEntity(functionality.name(),
                        toUppercase(functionality.getMenu().name()), toUppercase(functionality.getDescription())));
            }
        }
    }

    private List<PermissionEntity> removeExcessPermissions() {
        List<PermissionEntity> permissions = repository.findAll();
        if (permissions.size() > Functionality.values().length) {
            Iterator<PermissionEntity> iterator = permissions.iterator();
            while (iterator.hasNext()) {
                PermissionEntity entity = iterator.next();
                try {
                    Functionality.find(entity.getName());
                } catch (NotificationException e) {
                    repository.delete(entity);
                    iterator.remove();
                }
            }
        }
        return permissions;
    }
}
