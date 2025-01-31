package com.ctsousa.mover.domain;

import com.ctsousa.mover.core.entity.PermissionEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Permission extends DomainModel<PermissionEntity> {
    private String name;
    private String menu;
    private String description;

    @Override
    public PermissionEntity toEntity() {
        PermissionEntity entity = new PermissionEntity();
        entity.setId(this.getId());
        entity.setName(this.getName().toUpperCase());
        entity.setMenu(this.getMenu().toUpperCase());
        entity.setActive(this.active);
        entity.setName(this.getName());
        return entity;
    }
}
