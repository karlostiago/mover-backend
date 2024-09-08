package com.ctsousa.mover.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactRequest {

    private Long id;
    private Long clientId;
    private String name;
    private String telephone;
    private String degreeKinship;
    private Boolean active;
}
