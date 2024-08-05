package com.ctsousa.mover.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModelRequest {
    private Long id;
    private String name;
    private String color;
    private Integer yearManufacture;
    private Integer yearModel;
    private Long brandId;
    private Boolean active;

    public void setId(Long id) {
        if (id <= 0) {
            id = null;
        }
        this.id = id;
    }
}
