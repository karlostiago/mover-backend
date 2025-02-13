package com.ctsousa.mover.enumeration;

import com.ctsousa.mover.core.exception.notification.NotificationException;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum Functionality {

    REGISTER_CLIENTS(1, Menu.MY_CLIENTS, "Permite salvar clientes"),
    UPDATE_CLIENTS(2, Menu.MY_CLIENTS, "Permite atualizar clientes"),
    DELETE_CLIENTS(3, Menu.MY_CLIENTS, "Permite deletar clientes"),
    FILTER_CLIENTS(4, Menu.MY_CLIENTS, "Permite consultar clientes"),

    REGISTER_CONTRACTS(5, Menu.MY_CLIENTS, "Permite salvar contratos"),
    UPDATE_CONTRACTS(6, Menu.MY_CLIENTS, "Permite atualizar contratos"),
    DELETE_CONTRACTS(7, Menu.MY_CLIENTS, "Permite deletar contratos"),
    TERMINATION_CONTRACTS(8, Menu.MY_CLIENTS, "Permite encerrar contratos"),
    FILTER_CONTRACTS(9, Menu.MY_CLIENTS, "Permite consultar contratos"),

    REGISTER_VEHICLES(1, Menu.MY_FLEETS, "Permite salvar veículos"),
    UPDATE_VEHICLES(2, Menu.MY_FLEETS,"Permite atualizar veículos"),
    DELETE_VEHICLES(3, Menu.MY_FLEETS,"Permite deletar veículos"),
    VIEW_FIPE_VEHICLES(4, Menu.MY_FLEETS,"Permite visualizar tabela fipe"),
    FILTER_VEHICLES(5, Menu.MY_FLEETS,"Permite consultar veículos"),

    REGISTER_MAINTENANCE(6, Menu.MY_FLEETS,"Permite salvar manutenções"),
    UPDATE_MAINTENANCE(7, Menu.MY_FLEETS,"Permite atualizar manutenções"),
    DELETE_MAINTENANCE(8, Menu.MY_FLEETS,"Permite deletar manutenções"),
    FILTER_MAINTENANCE(9, Menu.MY_FLEETS,"Permite consultar manutenções"),

    REGISTER_TRANSACTIONS(1, Menu.MY_MONEY,"Permite salvar lançamentos"),
    UPDATE_TRANSACTIONS(2, Menu.MY_MONEY,"Permite atualizar lançamentos"),
    DELETE_TRANSACTIONS(3, Menu.MY_MONEY,"Permite deletar lançamentos"),
    PAYMENT_TRANSACTIONS(4, Menu.MY_MONEY,"Permite pagar lançamentos"),
    REFUND_TRANSACTIONS(5, Menu.MY_MONEY,"Permite estornar lançamentos"),
    ENABLED_INSTALLMENT_TRANSACTIONS(6, Menu.MY_MONEY,"Ativa repetição de lançamentos"),
    FILTER_TRANSACTIONS(7, Menu.MY_MONEY,"Permite consultar lançamentos"),

    REGISTER_ACCOUNTS(1, Menu.CONFIGURATION, "Permite salvar contas"),
    UPDATE_ACCOUNTS(2, Menu.CONFIGURATION, "Permite atualizar contas"),
    DELETE_ACCOUNTS(3, Menu.CONFIGURATION, "Permite deletar contas"),
    FILTER_ACCOUNTS(4, Menu.CONFIGURATION, "Permite consultar contas"),

    REGISTER_CARDS(5, Menu.CONFIGURATION, "Permite salvar cartões"),
    UPDATE_CARDS(6, Menu.CONFIGURATION, "Permite atualizar cartões"),
    DELETE_CARDS(7, Menu.CONFIGURATION, "Permite deletar cartões"),
    FILTER_CARDS(8, Menu.CONFIGURATION, "Permite consultar cartões"),

    REGISTER_CATEGORIES(9, Menu.CONFIGURATION, "Permite salvar categorias"),
    UPDATE_CATEGORIES(10, Menu.CONFIGURATION, "Permite atualizar categorias"),
    DELETE_CATEGORIES(11, Menu.CONFIGURATION, "Permite deletar categorias"),
    FILTER_CATEGORIES(12, Menu.CONFIGURATION, "Permite consultar categorias"),

    REGISTER_SUBCATEGORIES(13, Menu.CONFIGURATION, "Permite salvar subcategorias"),
    UPDATE_SUBCATEGORIES(1, Menu.CONFIGURATION, "Permite deletar subcategorias"),
    DELETE_SUBCATEGORIES(15, Menu.CONFIGURATION, "Permite atualizar subcategorias"),

    REGISTER_BRANDS(16, Menu.CONFIGURATION, "Permite salvar marcas"),
    UPDATE_BRANDS(17, Menu.CONFIGURATION, "Permite atualizar marcas"),
    DELETE_BRANDS(18, Menu.CONFIGURATION, "Permite deletar marcas"),
    FILTER_BRANDS(19, Menu.CONFIGURATION, "Permite consultar marcas"),

    REGISTER_MODELS(20, Menu.CONFIGURATION, "Permite salvar modelos"),
    UPDATE_MODELS(21, Menu.CONFIGURATION, "Permite atualizar modelos"),
    DELETE_MODELS(22, Menu.CONFIGURATION, "Permite deletar modelos"),
    FILTER_MODELS(23, Menu.CONFIGURATION, "Permite consultar modelos"),

    REGISTER_PARTNERS(24, Menu.CONFIGURATION, "Permite salvar sócios"),
    UPDATE_PARTNERS(25, Menu.CONFIGURATION, "Permite atualizar sócios"),
    DELETE_PARTNERS(26, Menu.CONFIGURATION, "Permite deletar sócios"),
    FILTER_PARTNERS(27, Menu.CONFIGURATION, "Permite consultar sócios"),

    REGISTER_PARAMETERS(28, Menu.CONFIGURATION, "Permite salvar parâmetros"),
    UPDATE_PARAMETERS(29, Menu.CONFIGURATION, "Permite atualizar parâmetros"),
    DELETE_PARAMETERS(30, Menu.CONFIGURATION, "Permite deletar parâmetros"),
    FILTER_PARAMETERS(31, Menu.CONFIGURATION, "Permite consultar parâmetros"),

    SEARCH_CARDS_ICONS(32, Menu.CONFIGURATION, "Permite consultar icones cartões"),
    SEARCH_ACCOUNTS_ICONS(33, Menu.CONFIGURATION, "Permite consultar icones contas"),
    UPLOAD_BRANDS(34, Menu.CONFIGURATION, "Permite carregar arquivos de marcas"),

    REGISTER_USERS(1, Menu.SECURITY, "Permite salvar usuários"),
    UPDATE_USERS(2, Menu.SECURITY, "Permite atualizar usuários"),
    DELETE_USERS(3, Menu.SECURITY, "Permite deletar usuários"),
    FILTER_USERS(4, Menu.SECURITY, "Permite consultar usuários"),

    REGISTER_PROFILES(5, Menu.SECURITY, "Permite salvar perfis"),
    UPDATE_PROFILES(6, Menu.SECURITY, "Permite atualizar perfis"),
    DELETE_PROFILES(7, Menu.SECURITY, "Permite deletar perfis"),
    FILTER_PROFILES(8, Menu.SECURITY, "Permite consultar perfis"),

    FILTER_PERMISSIONS(9, Menu.SECURITY, "Permite consultar permissões"),
    ACTIVATE_PERMISSIONS(10, Menu.SECURITY, "Permite ativar permissões"),
    DISABLED_PERMISSIONS(11, Menu.SECURITY, "Permite desativar permissões"),

    LOGIN_MOBILE(12, Menu.SECURITY, "Permite login para dispositivos moveis");

    private final Menu menu;
    private final Integer code;
    private final String description;

    Functionality(Integer code, Menu menu, String description) {
        this.code = code;
        this.menu = menu;
        this.description = description;
    }

    public static Functionality find(final String name) {
        List<Functionality> features = List.of(Functionality.values());
        return features.stream().filter(f -> f.name().equalsIgnoreCase(name))
                .findFirst().orElseThrow(() -> new NotificationException("Funcionalidade não encontrada."));
    }

    public static Functionality find(final int codeMenu, final int code) {
        List<Functionality> features = findByCodeMenu(codeMenu);
        return features.stream().filter(f -> f.code == code )
                .findFirst().orElseThrow(() -> new NotificationException("Funcionalidade não encontrada."));
    }

    private static List<Functionality> findByCodeMenu(final int code) {
        return Arrays.stream(Functionality.values())
                .filter(feature -> feature.getMenu().getCode() == code)
                .toList();
    }
}
