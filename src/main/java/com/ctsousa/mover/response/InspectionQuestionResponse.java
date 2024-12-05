package com.ctsousa.mover.response;

import com.ctsousa.mover.enumeration.ResponseEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InspectionQuestionResponse {

    private InspectionResponse inspection;

    private QuestionResponse question;

    private ResponseEnum response;
}
