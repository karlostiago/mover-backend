package com.ctsousa.mover.response;

import com.ctsousa.mover.enumeration.TypeCategory;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryResponse {
    private Long id;
    private String description;
    private String type;
    private Boolean active;

    public void setType(TypeCategory type) {
        this.type = type.getDescription();
    }
}
