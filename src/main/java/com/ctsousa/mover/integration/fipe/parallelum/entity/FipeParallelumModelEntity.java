package com.ctsousa.mover.integration.fipe.parallelum.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FipeParallelumModelEntity {
    private String code;
    private String name;

    public FipeParallelumModelEntity() { }

    public FipeParallelumModelEntity(String code) {
        setCode(code);
    }
}
