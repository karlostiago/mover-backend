package com.ctsousa.mover.domain;

import com.ctsousa.mover.core.entity.PermissionEntity;
import com.ctsousa.mover.core.entity.ProfileEntity;
import com.ctsousa.mover.enumeration.Functionality;
import com.ctsousa.mover.response.FuncionalityResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static com.ctsousa.mover.core.util.StringUtil.toUppercase;

@Getter
@Setter
public class Profile extends DomainModel<ProfileEntity> {
    private String description;
    private List<FuncionalityResponse> permissions;

    @Override
    public ProfileEntity toEntity() {
        ProfileEntity entity = new ProfileEntity();
        entity.setId(this.getId());
        entity.setDescription(toUppercase(this.getDescription()));
        entity.setActive(this.active);

        List<PermissionEntity> permissions = this.permissions.stream()
                .map(this::createPermissionEntity)
                .toList();

        entity.setPermissions(permissions);

        return entity;
    }

    private PermissionEntity createPermissionEntity(FuncionalityResponse permission) {
        Functionality functionality = Functionality.find(permission.getCodeMenu(), permission.getId());
        return new PermissionEntity(functionality.name(), toUppercase(functionality.getMenu().getDescription()), toUppercase(functionality.getDescription()));
    }
}
