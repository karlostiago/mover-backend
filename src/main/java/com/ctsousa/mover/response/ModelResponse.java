package com.ctsousa.mover.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModelResponse {
    private Long id;
    private String name;
    private Long brandId;
    private String brandName;
    private Boolean active;
}
