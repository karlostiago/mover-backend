package com.ctsousa.mover.integration.fipe.parallelum.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FipeParallelumBrandEntity {
    private String code;
    private String name;

    public FipeParallelumBrandEntity() { }

    public FipeParallelumBrandEntity(String code) {
        setCode(code);
    }
}
