package com.ctsousa.mover.domain;

import com.ctsousa.mover.core.entity.CategoryEntity;
import com.ctsousa.mover.core.entity.SubCategoryEntity;
import lombok.Getter;
import lombok.Setter;

import static com.ctsousa.mover.core.util.StringUtil.toUppercase;

@Getter
@Setter
public class SubCategory extends DomainModel<SubCategoryEntity> {

    private String description;
    private Long categoryId;
    private Boolean active;

    @Override
    public SubCategoryEntity toEntity() {
        SubCategoryEntity entity = new SubCategoryEntity();
        entity.setId(this.getId());
        entity.setDescription(toUppercase(this.getDescription()));
        entity.setCategory(new CategoryEntity(getCategoryId()));
        entity.setActive(this.getActive());
        return entity;
    }
}
