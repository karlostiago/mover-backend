package com.ctsousa.mover.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartnerResponse {
    private Long id;
    private String name;
    private String email;
    private Boolean active;
}
