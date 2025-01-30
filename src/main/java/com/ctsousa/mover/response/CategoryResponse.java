package com.ctsousa.mover.response;

import com.ctsousa.mover.domain.SubCategory;
import com.ctsousa.mover.enumeration.TypeCategory;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CategoryResponse {
    private Long id;
    private String description;
    private String type;
    private String originalType;
    private Boolean active;

    private List<SubCategory> subcategories = new ArrayList<>();

    public void setType(TypeCategory type) {
        this.type = type.getDescription();
        this.originalType = type.name();
    }
}
