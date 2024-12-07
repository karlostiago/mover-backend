package com.ctsousa.mover.response;


import com.ctsousa.mover.enumeration.QuestionnaireType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class QuestionnaireResponse {

    private String description;

    private QuestionnaireType questionnaireType;

    private List<QuestionResponse> questions = new ArrayList<>();
}
