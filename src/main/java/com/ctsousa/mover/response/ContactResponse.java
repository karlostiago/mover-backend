package com.ctsousa.mover.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactResponse {
    private Long id;
    private Long clientId;
    private String name;
    private String telephone;
    private String degreeKinship;
    private Boolean active;
}
