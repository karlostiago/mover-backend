package com.ctsousa.mover.response;

import com.ctsousa.mover.enumeration.QuestionnaireType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class QuestionResponse {

    private String description;

    private QuestionnaireType questionnaireType;

    private QuestionnaireResponse questionnaire;
}
