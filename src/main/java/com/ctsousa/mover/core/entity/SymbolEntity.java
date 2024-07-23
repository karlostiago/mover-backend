package com.ctsousa.mover.core.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_symbol")
public class SymbolEntity extends AbstractEntity {

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "image_base64", columnDefinition = "LONGTEXT")
    private String imageBase64;
}
