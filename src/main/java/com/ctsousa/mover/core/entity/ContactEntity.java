package com.ctsousa.mover.core.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_contact")
public class ContactEntity extends AbstractEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id", nullable = false)
    private ClientEntity client;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "telephone", nullable = false)
    private String telephone;

    @Column(name = "degreeKinship", nullable = false)
    private String degreeKinship;
}
