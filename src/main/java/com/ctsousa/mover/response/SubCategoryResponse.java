package com.ctsousa.mover.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubCategoryResponse {
    private Long id;
    private String description;
    private Long categoryId;
    private String categoryDescription;
    private Boolean active;
}
