package com.ctsousa.mover.domain;

import com.ctsousa.mover.core.entity.ProfileEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;

@Getter
@Setter
public class Profile extends DomainModel<ProfileEntity> {
    private String description;
    private List<Permission> permissions;

    @Override
    public ProfileEntity toEntity() {
        ProfileEntity entity = new ProfileEntity();
        entity.setId(this.getId());
        entity.setDescription(this.getDescription().toUpperCase());
        entity.setActive(this.active);



        return entity;
    }
}
