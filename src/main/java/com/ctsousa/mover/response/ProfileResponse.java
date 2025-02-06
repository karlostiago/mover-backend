package com.ctsousa.mover.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProfileResponse {
    private Long id;
    private String name;
    private List<FuncionalityResponse> features;
    private Boolean active;
}
