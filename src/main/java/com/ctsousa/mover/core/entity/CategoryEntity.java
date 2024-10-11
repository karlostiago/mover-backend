package com.ctsousa.mover.core.entity;

import com.ctsousa.mover.enumeration.TypeCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "tb_category")
public class CategoryEntity extends AbstractEntity {

    @Column(name = "description", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TypeCategory type;

    public CategoryEntity() { }

    public CategoryEntity(Long id) {
        super.setId(id);
    }
}
