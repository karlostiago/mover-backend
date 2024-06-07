package com.ctsousa.mover.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Subcategory extends AbstractEntity {

    private String description;
    private CategoryEntity category;
}
