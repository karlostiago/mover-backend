package com.ctsousa.mover.request;

import com.ctsousa.mover.core.annotation.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryRequest {
    private Long id;

    @NotEmpty(message = "Nome não pode ser vázio")
    private String description;

    @NotEmpty(message = "Tipo não pode ser vázio")
    private String type;

    private Boolean active;
}
