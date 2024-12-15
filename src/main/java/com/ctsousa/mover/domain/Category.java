package com.ctsousa.mover.domain;

import com.ctsousa.mover.core.entity.CategoryEntity;
import com.ctsousa.mover.core.entity.SubCategoryEntity;
import com.ctsousa.mover.enumeration.TypeCategory;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static com.ctsousa.mover.core.util.StringUtil.toUppercase;

@Getter
@Setter
public class Category extends DomainModel<CategoryEntity> {

    private String description;
    private String type;
    private Boolean active;

    private List<SubCategory> subcategories = new ArrayList<>();

    @Override
    public CategoryEntity toEntity() {
        CategoryEntity entity = new CategoryEntity();
        entity.setId(this.getId());
        entity.setDescription(toUppercase(this.getDescription()));
        entity.setType(TypeCategory.toDescription(this.getType()));
        entity.setActive(this.getActive());

        if (!subcategories.isEmpty()) {
            for (SubCategory subcategory : subcategories) {
                SubCategoryEntity subcategoryEntity = new SubCategoryEntity();
                subcategoryEntity.setCategory(entity);
                subcategoryEntity.setId(this.getId() == null ? null : subcategory.getId());
                subcategoryEntity.setActive(subcategory.getActive());
                subcategoryEntity.setDescription(toUppercase(subcategory.getDescription()));
                entity.getSubcategories().add(subcategoryEntity);
            }
        }

        return entity;
    }
}
