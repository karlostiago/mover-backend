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
@Table(name = "tb_photo")
public class Photo extends AbstractEntity {

    @Column(nullable = false, name = "image")
    private String image;

    @Column(nullable = false, name = "required")
    private boolean required;
}
