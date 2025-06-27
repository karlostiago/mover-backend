package com.ctsousa.mover.integration.corpvs.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Address {
    private Location finalAddress;
    private Location startingAddress;
}
