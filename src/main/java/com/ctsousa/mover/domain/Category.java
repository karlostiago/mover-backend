package com.ctsousa.mover.domain;

import com.ctsousa.mover.core.entity.CategoryEntity;
import com.ctsousa.mover.core.entity.ConfigurationEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.enumeration.TypeCategory;
import com.ctsousa.mover.enumeration.TypeValueConfiguration;
import lombok.Getter;
import lombok.Setter;

import static com.ctsousa.mover.core.util.StringUtil.toUppercase;

@Getter
@Setter
public class Category extends DomainModel<CategoryEntity> {

    private String description;
    private String type;
    private Boolean active;

    @Override
    public CategoryEntity toEntity() {
        CategoryEntity entity = new CategoryEntity();
        entity.setId(this.getId());
        entity.setDescription(toUppercase(this.getDescription()));
        entity.setType(TypeCategory.toDescription(this.getType()));
        entity.setActive(this.getActive());
        return entity;
    }
}