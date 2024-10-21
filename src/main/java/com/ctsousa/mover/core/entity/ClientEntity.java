package com.ctsousa.mover.core.entity;

import com.ctsousa.mover.enumeration.TypePerson;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "tb_client")
public class ClientEntity extends AbstractEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "rg", nullable = false)
    private String rg;

    @Column(name = "cpfCnpj", nullable = false)
    private String cpfCnpj;

    @Column(name = "number", nullable = false)
    private String number;

    @Column(name = "mother_name", nullable = false)
    private String motherName;

    @Column(name = "state", nullable = false)
    private String state;

    @Column(name = "neighborhood", nullable = false)
    private String neighborhood;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "complement")
    private String complement;

    @Column(name = "public_place", nullable = false)
    private String publicPlace;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_person", nullable = false)
    private TypePerson typePerson;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "cell_phone", nullable = false)
    private String cellPhone;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContactEntity> contacts = new ArrayList<>();

    public ClientEntity() { }

    public ClientEntity(Long id) {
        super.setId(id);
    }
}
