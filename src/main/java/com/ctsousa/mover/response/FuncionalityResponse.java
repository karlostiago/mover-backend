package com.ctsousa.mover.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FuncionalityResponse {
    private Integer id;
    private String name;
    private String menuName;
    private Integer codeMenu;
    private Boolean active;
}
