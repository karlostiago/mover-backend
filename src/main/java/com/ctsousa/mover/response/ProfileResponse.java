package com.ctsousa.mover.response;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProfileResponse {
    private Long id;
    private String description;
    private List<FuncionalityResponse> permissions = new ArrayList<>();
    private Boolean active;
}
