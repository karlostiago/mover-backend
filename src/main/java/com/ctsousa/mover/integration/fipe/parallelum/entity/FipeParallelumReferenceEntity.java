package com.ctsousa.mover.integration.fipe.parallelum.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FipeParallelumReferenceEntity {
    private String code;
    private String month;

    public FipeParallelumReferenceEntity() { }

    public FipeParallelumReferenceEntity(String code) {
        setCode(code);
    }
}
