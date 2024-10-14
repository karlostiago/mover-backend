package com.ctsousa.mover.response;

import com.ctsousa.mover.enumeration.TypeCategory;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubCategoryResponse {
    private Long id;
    private String description;
    private String categoryType;
    private Long categoryId;
    private String categoryDescription;
    private Boolean active;

    public void setCategoryType(TypeCategory typeCategory) {
        this.categoryType = typeCategory.getDescription();
    }
}
