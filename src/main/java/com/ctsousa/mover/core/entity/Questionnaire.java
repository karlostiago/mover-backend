package com.ctsousa.mover.core.entity;

import com.ctsousa.mover.enumeration.QuestionnaireType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_questionare")
public class Questionnaire extends AbstractEntity {

    @Column(nullable = false,
            name = "description"
    )
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "questionnaire_type",
            nullable = false
    )
    private QuestionnaireType questionnaireType;

    @OneToMany(mappedBy = "questionnaire",
               cascade = CascadeType.ALL,
               orphanRemoval = true
    )
    private List<Question> questions = new ArrayList<>();
}