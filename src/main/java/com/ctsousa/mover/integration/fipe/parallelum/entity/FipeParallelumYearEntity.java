package com.ctsousa.mover.integration.fipe.parallelum.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FipeParallelumYearEntity {
    private String code;
    private String name;

    public FipeParallelumYearEntity() { }

    public FipeParallelumYearEntity(String code) {
        setCode(code);
    }
}
