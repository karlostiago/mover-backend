package com.ctsousa.mover.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContactEntity extends AbstractEntity {

    private ClientEntity client;
    private String description;
    private String name;
    private String phone;
}
