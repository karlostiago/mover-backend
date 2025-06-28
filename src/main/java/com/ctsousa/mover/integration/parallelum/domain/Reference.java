package com.ctsousa.mover.integration.parallelum.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Reference extends Domain {

    private String month;

    public Reference() { }

    public Reference(String code) {
        setCode(code);
    }
}
