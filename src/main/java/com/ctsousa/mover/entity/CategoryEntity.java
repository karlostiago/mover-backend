package com.ctsousa.mover.entity;

import com.ctsousa.mover.enumeration.TypeEntry;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryEntity extends AbstractEntity {

    private String decription;
    private TypeEntry type;
}
