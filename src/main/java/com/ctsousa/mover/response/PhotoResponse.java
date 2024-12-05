package com.ctsousa.mover.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhotoResponse {

    private String image;

    private boolean mandatory;

    private QuestionnaireResponse questionnaire;
}
