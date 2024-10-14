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
@Table(name = "tb_photo")
public class PhotoEntity extends AbstractEntity {

    @Column(nullable = false, name = "image", columnDefinition = "LONGTEXT")
    private String image;

    @Column(nullable = false,name = "mandatory")
    private boolean mandatory;

    @ManyToOne
    @JoinColumn(name = "questionnaire_id")
    private QuestionnaireEntity questionnaire;
}
