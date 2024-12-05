package com.ctsousa.mover.core.entity;

import com.ctsousa.mover.enumeration.ResponseEnum;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_inspection_question")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InspectionQuestionEntity extends AbstractEntity{

    @ManyToOne
    @JoinColumn(name = "inspection_id")
    @JsonBackReference
    private InspectionEntity inspection;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private QuestionEntity question;

    @Enumerated(EnumType.STRING)
    @Column(name = "response_enum", nullable = false)
    private ResponseEnum response;
}
