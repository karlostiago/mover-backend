package com.ctsousa.mover.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SymbolResponse {

    private Long id;

    private String description;

    private String imageBase64;
}
