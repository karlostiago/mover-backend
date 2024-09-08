package com.ctsousa.mover.core.entity;

import com.ctsousa.mover.enumeration.ResponseEnum;
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
public class InspectionQuestion extends AbstractEntity{

    @ManyToOne
    @JoinColumn(name = "inspection_id")
    private Inspection inspection;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    @Enumerated(EnumType.STRING)
    @Column(name = "response_enum", nullable = false)
    private ResponseEnum response;
}
