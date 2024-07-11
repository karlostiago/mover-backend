package com.ctsousa.mover.service.querys;

public interface ClientQuerys {
    public static final String EXISTING_CPF_CLIENT_APPLICATION =
            "SELECT c FROM ClientEntity c WHERE translate(c.cpf, '.-', '') = translate(?1, '.-', '')";

}
