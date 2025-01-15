package com.ctsousa.mover.enumeration;

import lombok.Getter;

@Getter
public enum Functionality {

    REGISTER_CLIENTS(1, Menu.MY_CLIENTS, "Cadastro de clientes"),
    UPDATE_CLIENTS(2, Menu.MY_CLIENTS, "Atualizar clientes"),
    DELETE_CLIENTS(3, Menu.MY_CLIENTS, "Deletar clientes"),
    FILTER_CLIENTS(4, Menu.MY_CLIENTS, "Consultar clientes"),

    REGISTER_CONTRACTS(5, Menu.MY_CLIENTS, "Cadastro de contratos"),
    UPDATE_CONTRACTS(6, Menu.MY_CLIENTS, "Atualizar contratos"),
    DELETE_CONTRACTS(7, Menu.MY_CLIENTS, "Deletar contratos"),
    TERMINATION_CONTRACTS(8, Menu.MY_CLIENTS, "Encerrar contratos"),
    FILTER_CONTRACTS(9, Menu.MY_CLIENTS, "Consultar contratos"),

    REGISTER_VEHICLES(1, Menu.MY_FLEETS, "Cadastro de veiculos"),
    UPDATE_VEHICLES(2, Menu.MY_FLEETS,"Atualizar veiculos"),
    DELETE_VEHICLES(3, Menu.MY_FLEETS,"Deletar veiculos"),
    VIEW_FIPE_VEHICLES(4, Menu.MY_FLEETS,"Ver tabela fipe"),
    FILTER_VEHICLES(5, Menu.MY_FLEETS,"Consultar veiculos"),

    REGISTER_MAINTENANCE(6, Menu.MY_FLEETS,"Cadastro de manutenções"),
    UPDATE_MAINTENANCE(7, Menu.MY_FLEETS,"Atualizar manutenções"),
    DELETE_MAINTENANCE(8, Menu.MY_FLEETS,"Deletar manutenções"),
    FILTER_MAINTENANCE(9, Menu.MY_FLEETS,"Consultar manutenções"),

    REGISTER_TRANSACTIONS(1, Menu.MY_MONEY,"Cadastro de lançamentos"),
    UPDATE_TRANSACTIONS(2, Menu.MY_MONEY,"Atualizar lançamentos"),
    DELETE_TRANSACTIONS(3, Menu.MY_MONEY,"Deletar lançamentos"),
    PAYMENT_TRANSACTIONS(4, Menu.MY_MONEY,"Pagar lançamentos"),
    REFUND_TRANSACTIONS(5, Menu.MY_MONEY,"Estornar lançamentos"),
    ENABLED_INSTALLMENT_TRANSACTIONS(6, Menu.MY_MONEY,"Habilitar parcelamento de lançamentos"),
    FILTER_TRANSACTIONS(7, Menu.MY_MONEY,"Consultar lançamentos"),

    REGISTER_ACCOUNTS(1, Menu.CONFIGURATION, "Cadastro de contas"),
    UPDATE_ACCOUNTS(2, Menu.CONFIGURATION, "Atualizar contas"),
    DELETE_ACCOUNTS(3, Menu.CONFIGURATION, "Deletar contas"),
    FILTER_ACCOUNTS(4, Menu.CONFIGURATION, "Consultar contas"),

    REGISTER_CARDS(5, Menu.CONFIGURATION, "Cadastro de cartões"),
    UPDATE_CARDS(6, Menu.CONFIGURATION, "Atualizar cartões"),
    DELETE_CARDS(7, Menu.CONFIGURATION, "Deletar cartões"),
    FILTER_CARDS(8, Menu.CONFIGURATION, "Consultar cartões"),

    REGISTER_CATEGORIES(9, Menu.CONFIGURATION, "Cadastro de categorias"),
    UPDATE_CATEGORIES(10, Menu.CONFIGURATION, "Atualizar categorias"),
    DELETE_CATEGORIES(11, Menu.CONFIGURATION, "Deletar categorias"),
    FILTER_CATEGORIES(12, Menu.CONFIGURATION, "Consultar categorias"),

    REGISTER_SUBCATEGORIES(13, Menu.CONFIGURATION, "Cadastrar subcategorias"),
    UPDATE_SUBCATEGORIES(1, Menu.CONFIGURATION, "Deletar subcategorias"),
    DELETE_SUBCATEGORIES(15, Menu.CONFIGURATION, "Atualizar subcategorias"),

    REGISTER_BRANDS(16, Menu.CONFIGURATION, "Cadastro de marcas"),
    UPDATE_BRANDS(17, Menu.CONFIGURATION, "Atualizar marcas"),
    DELETE_BRANDS(18, Menu.CONFIGURATION, "Deletar marcas"),
    FILTER_BRANDS(19, Menu.CONFIGURATION, "Consultar marcas"),

    REGISTER_MODELS(20, Menu.CONFIGURATION, "Cadastro de modelos"),
    UPDATE_MODELS(21, Menu.CONFIGURATION, "Atualizar modelos"),
    DELETE_MODELS(22, Menu.CONFIGURATION, "Deletar modelos"),
    FILTER_MODELS(23, Menu.CONFIGURATION, "Consultar modelos"),

    REGISTER_PARTNERS(24, Menu.CONFIGURATION, "Cadastro de sócios"),
    UPDATE_PARTNERS(25, Menu.CONFIGURATION, "Atualizar sócios"),
    DELETE_PARTNERS(26, Menu.CONFIGURATION, "Deletar sócios"),
    FILTER_PARTNERS(27, Menu.CONFIGURATION, "Consultar sócios"),

    REGISTER_PARAMETERS(28, Menu.CONFIGURATION, "Cadastro de parâmetros"),
    UPDATE_PARAMETERS(29, Menu.CONFIGURATION, "Atualizar parâmetros"),
    DELETE_PARAMETERS(30, Menu.CONFIGURATION, "Deletar parâmetros"),
    FILTER_PARAMETERS(31, Menu.CONFIGURATION, "Consultar parâmetros");

    private final Menu menu;
    private final Integer code;
    private final String description;

    Functionality(Integer code, Menu menu, String description) {
        this.code = code;
        this.menu = menu;
        this.description = description;
    }
}
