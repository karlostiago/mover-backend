package com.ctsousa.mover.request;

import com.ctsousa.mover.core.annotation.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubCategoryRequest {
    private Long id;

    @NotEmpty(message = "Nome não pode ser vázio")
    private String description;

    @NotEmpty(message = "Categoria não pode ser vázio")
    private Long categoryId;

    private Boolean active;
}
