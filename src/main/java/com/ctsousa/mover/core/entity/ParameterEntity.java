package com.ctsousa.mover.core.entity;

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
@Table(name = "tb_parameter")
public class ParameterEntity extends AbstractEntity {

    @Column(name = "`key`", nullable = false)
    private String key;

    @Column(name = "`value`", nullable = false)
    private String value;

    @Column(name = "type_value", nullable = false)
    private String typeValue;
}
