package com.ctsousa.mover.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_client")
public class ClientEntity extends AbstractEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "rg", nullable = false)
    private String rg;

    @Column(name = "cpf", nullable = false)
    private String cpf;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "email")
    private String email;

    @Column(name = "mother_name")
    private String motherName;

    @Column(name = "public_place")
    private String publicPlace;

    @Column(name = "number", nullable = false)
    private String number;

    @Column(name = "complement")
    private String complement;

    @Column(name = "district")
    private String district;

    @Column(name = "state", nullable = false)
    private String state;

    @Column(name = "cep", nullable = false)
    private String cep;

    @Column(name = "attachment")
    private String attachment;
}
