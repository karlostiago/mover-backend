package com.ctsousa.mover.domain;

import com.ctsousa.mover.core.entity.PermissionEntity;
import com.ctsousa.mover.core.entity.ProfileEntity;
import com.ctsousa.mover.enumeration.Functionality;
import com.ctsousa.mover.response.FuncionalityResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;

@Getter
@Setter
public class Profile extends DomainModel<ProfileEntity> {
    private String description;
    private List<FuncionalityResponse> permissions;

    @Override
    public ProfileEntity toEntity() {
        ProfileEntity entity = new ProfileEntity();
        entity.setId(this.getId());
        entity.setDescription(this.getDescription().toUpperCase());
        entity.setActive(this.active);

        for (FuncionalityResponse permission : permissions) {
            Functionality functionality = Functionality.find(permission.getCodeMenu(), permission.getId());
            PermissionEntity permissionEntity = new PermissionEntity();
            permissionEntity.setName(functionality.name());
            permissionEntity.setDescription(functionality.getDescription().toUpperCase());
            permissionEntity.setMenu(functionality.getMenu().getDescription().toUpperCase());
            permissionEntity.setActive(this.getActive());
            entity.getPermissions().add(permissionEntity);
        }

        return entity;
    }
}
