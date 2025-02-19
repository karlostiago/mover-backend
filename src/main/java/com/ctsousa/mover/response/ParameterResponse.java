package com.ctsousa.mover.response;

import com.ctsousa.mover.enumeration.TypeValueParameter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParameterResponse {
    private Long id;
    private String key;
    private String value;
    private String typeValue;
    private Boolean active;

    public void setTypeValue(String typeValue) {
        this.typeValue = String.valueOf(TypeValueParameter.toDescription(typeValue).getCode());
    }
}
