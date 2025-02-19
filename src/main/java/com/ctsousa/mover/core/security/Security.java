package com.ctsousa.mover.core.security;

public abstract class Security {

    public static class PreAutorize {
        public static class User {
            public final static String REGISTER_USERS = "hasAnyRole('ROLE_REGISTER_USERS')";
            public final static String UPDATE_USERS = "hasAnyRole('ROLE_UPDATE_USERS')";
            public final static String DELETE_USERS = "hasAnyRole('ROLE_DELETE_USERS')";
            public final static String FILTER_USERS = "hasAnyRole('ROLE_FILTER_USERS')";
            public final static String LOGIN_MOBILE = "hasAnyRole('ROLE_LOGIN_MOBILE')";
        }

        public static class ChangePassword {
            public final static String FILTER_USERS = "hasAnyRole('ROLE_FILTER_CHANGEPASSWORD_USERS')";
            public final static String UPDATE_CHANGEPASSWORD_USERS = "hasAnyRole('ROLE_UPDATE_CHANGEPASSWORD_USERS')";
        }

        public static class Profile {
            public final static String REGISTER_PROFILES = "hasAnyRole('ROLE_REGISTER_PROFILES')";
            public final static String UPDATE_PROFILES = "hasAnyRole('ROLE_UPDATE_PROFILES')";
            public final static String DELETE_PROFILES = "hasAnyRole('ROLE_DELETE_PROFILES')";
            public final static String FILTER_PROFILES = "hasAnyRole('ROLE_FILTER_PROFILES')";
        }

        public static class Parameter {
            public final static String REGISTER_PARAMETERS = "hasAnyRole('ROLE_REGISTER_PARAMETERS')";
            public final static String UPDATE_PARAMETERS = "hasAnyRole('ROLE_UPDATE_PARAMETERS')";
            public final static String DELETE_PARAMETERS = "hasAnyRole('ROLE_DELETE_PARAMETERS')";
            public final static String FILTER_PARAMETERS = "hasAnyRole('ROLE_FILTER_PARAMETERS')";
        }

        public static class Partner {
            public final static String REGISTER_PARTNERS = "hasAnyRole('ROLE_REGISTER_PARTNERS')";
            public final static String UPDATE_PARTNERS = "hasAnyRole('ROLE_UPDATE_PARTNERS')";
            public final static String DELETE_PARTNERS = "hasAnyRole('ROLE_DELETE_PARTNERS')";
            public final static String FILTER_PARTNERS = "hasAnyRole('ROLE_FILTER_PARTNERS', 'FILTER_PARTNES_TRANSACTIONS')";
        }

        public static class Model {
            public final static String REGISTER_MODELS = "hasAnyRole('ROLE_REGISTER_MODELS')";
            public final static String UPDATE_MODELS = "hasAnyRole('ROLE_UPDATE_MODELS')";
            public final static String DELETE_MODELS = "hasAnyRole('ROLE_DELETE_MODELS')";
            public final static String FILTER_MODELS = "hasAnyRole('ROLE_FILTER_MODELS')";
        }

        public static class Brand {
            public final static String REGISTER_BRANDS = "hasAnyRole('ROLE_REGISTER_BRANDS')";
            public final static String UPDATE_BRANDS = "hasAnyRole('ROLE_UPDATE_BRANDS')";
            public final static String DELETE_BRANDS = "hasAnyRole('ROLE_DELETE_BRANDS')";
            public final static String FILTER_BRANDS = "hasAnyRole('ROLE_FILTER_BRANDS')";
            public final static String UPLOAD_BRANDS = "hasAnyRole('ROLE_UPLOAD_BRANDS')";
        }

        public static class Category {
            public final static String REGISTER_CATEGORIES = "hasAnyRole('ROLE_REGISTER_CATEGORIES')";
            public final static String UPDATE_CATEGORIES = "hasAnyRole('ROLE_UPDATE_CATEGORIES')";
            public final static String DELETE_CATEGORIES = "hasAnyRole('ROLE_DELETE_CATEGORIES')";
            public final static String FILTER_CATEGORIES = "hasAnyRole('ROLE_FILTER_CATEGORIES', 'ROLE_FILTER_TYPE_CATEGORIES_TRANSACTIONS', 'ROLE_FILTER_CATEGORIES_TRANSACTIONS')";
        }

        public static class Card {
            public final static String REGISTER_CARDS = "hasAnyRole('ROLE_FILTER_CARDS')";
            public final static String UPDATE_CARDS = "hasAnyRole('ROLE_FILTER_CARDS')";
            public final static String DELETE_CARDS = "hasAnyRole('ROLE_FILTER_CARDS')";
            public final static String FILTER_CARDS = "hasAnyRole('ROLE_FILTER_CARDS', 'ROLE_FILTER_CARDS_TRANSACTIONS')";
            public final static String SEARCH_CARDS_ICONS = "hasAnyRole('ROLE_SEARCH_CARDS_ICONS')";
        }

        public static class Account {
            public final static String REGISTER_ACCOUNTS = "hasAnyRole('ROLE_REGISTER_ACCOUNTS')";
            public final static String UPDATE_ACCOUNTS = "hasAnyRole('ROLE_UPDATE_ACCOUNTS')";
            public final static String DELETE_ACCOUNTS = "hasAnyRole('ROLE_DELETE_ACCOUNTS')";
            public final static String FILTER_ACCOUNTS = "hasAnyRole('ROLE_FILTER_ACCOUNTS', 'ROLE_FILTER_ACCOUNT_TRANSACTIONS')";
            public final static String SEARCH_ACCOUNTS_ICONS = "hasAnyRole('ROLE_SEARCH_ACCOUNTS_ICONS')";
        }

        public static class Transaction {
            public final static String REGISTER_TRANSACTIONS = "hasAnyRole('ROLE_REGISTER_TRANSACTIONS')";
            public final static String UPDATE_TRANSACTIONS = "hasAnyRole('ROLE_UPDATE_TRANSACTIONS')";
            public final static String DELETE_TRANSACTIONS = "hasAnyRole('ROLE_DELETE_TRANSACTIONS')";
            public final static String PAYMENT_TRANSACTIONS = "hasAnyRole('ROLE_PAYMENT_TRANSACTIONS')";
            public final static String REFUND_TRANSACTIONS = "hasAnyRole('ROLE_REFUND_TRANSACTIONS')";
            public final static String FILTER_TRANSACTIONS = "hasAnyRole('ROLE_FILTER_TRANSACTIONS')";
        }

        public static class Maintenance {
            public final static String REGISTER_MAINTENANCE = "hasAnyRole('ROLE_REGISTER_MAINTENANCE')";
            public final static String UPDATE_MAINTENANCE = "hasAnyRole('ROLE_UPDATE_MAINTENANCE')";
            public final static String DELETE_MAINTENANCE = "hasAnyRole('ROLE_DELETE_MAINTENANCE')";
            public final static String FILTER_MAINTENANCE = "hasAnyRole('ROLE_FILTER_MAINTENANCE')";
        }

        public static class Vehicle {
            public final static String REGISTER_VEHICLES = "hasAnyRole('ROLE_REGISTER_VEHICLES')";
            public final static String UPDATE_VEHICLES = "hasAnyRole('ROLE_UPDATE_VEHICLES')";
            public final static String DELETE_VEHICLES = "hasAnyRole('ROLE_DELETE_VEHICLES')";
            public final static String VIEW_FIPE_VEHICLES = "hasAnyRole('ROLE_VIEW_FIPE_VEHICLES')";
            public final static String FILTER_VEHICLES = "hasAnyRole('ROLE_FILTER_VEHICLES', 'ROLE_FILTER_VEHICLES_TRANSACTIONS')";
        }

        public static class Contract {
            public final static String REGISTER_CONTRACTS = "hasAnyRole('ROLE_REGISTER_CONTRACTS')";
            public final static String UPDATE_CONTRACTS = "hasAnyRole('ROLE_UPDATE_CONTRACTS')";
            public final static String DELETE_CONTRACTS = "hasAnyRole('ROLE_DELETE_CONTRACTS')";
            public final static String TERMINATION_CONTRACTS = "hasAnyRole('ROLE_TERMINATION_CONTRACTS')";
            public final static String FILTER_CONTRACTS = "hasAnyRole('ROLE_FILTER_CONTRACTS', 'ROLE_FILTER_CONTRACTS_TRANSACTIONS')";
        }

        public static class Client {
            public final static String REGISTER_CLIENTS = "hasAnyRole('ROLE_REGISTER_CLIENTS')";
            public final static String UPDATE_CLIENTS = "hasAnyRole('ROLE_UPDATE_CLIENTS')";
            public final static String DELETE_CLIENTS = "hasAnyRole('ROLE_DELETE_CLIENTS')";
            public final static String FILTER_CLIENTS = "hasAnyRole('ROLE_FILTER_CLIENTS')";
            public final static String FILTER_TYPE_PERSON_CLIENTS = "hasAnyRole('ROLE_FILTER_TYPE_PERSON_CLIENTS')";
            public final static String FILTER_BRAZILIAN_STATES_CLIENTS = "hasAnyRole('ROLE_FILTER_BRAZILIAN_STATES_CLIENTS')";
        }
    }
}
