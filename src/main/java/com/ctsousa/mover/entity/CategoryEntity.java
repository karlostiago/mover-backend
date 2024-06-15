package com.ctsousa.mover.entity;

import com.ctsousa.mover.enumeration.TypeEntry;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "category")
public class CategoryEntity extends AbstractEntity {

    @Column(name = "description", nullable = false)
    private String decription;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TypeEntry type;
}
