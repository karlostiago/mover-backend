package com.ctsousa.mover.integration.corpvs.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Location {
    private String description;
    private Integer distance;
    private String distanceFormat;
}
