package com.ctsousa.mover.core.security;

public abstract class Security {

    public static class PreAutorize {
        public static class User {
            public final static String REGISTER_USERS = "hasRole('ROLE_REGISTER_USERS')";
            public final static String UPDATE_USERS = "hasRole('ROLE_UPDATE_USERS')";
            public final static String DELETE_USERS = "hasRole('ROLE_DELETE_USERS')";
            public final static String FILTER_USERS = "hasRole('ROLE_FILTER_USERS')";
            public final static String LOGIN_MOBILE = "hasRole('ROLE_LOGIN_MOBILE')";
        }

        public static class Profile {
            public final static String REGISTER_PROFILES = "hasRole('ROLE_REGISTER_PROFILES')";
            public final static String UPDATE_PROFILES = "hasRole('ROLE_UPDATE_PROFILES')";
            public final static String DELETE_PROFILES = "hasRole('ROLE_DELETE_PROFILES')";
            public final static String FILTER_PROFILES = "hasRole('ROLE_FILTER_PROFILES')";
        }

        public static class Parameter {
            public final static String REGISTER_PARAMETERS = "hasRole('ROLE_REGISTER_PARAMETERS')";
            public final static String UPDATE_PARAMETERS = "hasRole('ROLE_UPDATE_PARAMETERS')";
            public final static String DELETE_PARAMETERS = "hasRole('ROLE_DELETE_PARAMETERS')";
            public final static String FILTER_PARAMETERS = "hasRole('ROLE_FILTER_PARAMETERS')";
        }

        public static class Partner {
            public final static String REGISTER_PARTNERS = "hasRole('ROLE_REGISTER_PARTNERS')";
            public final static String UPDATE_PARTNERS = "hasRole('ROLE_UPDATE_PARTNERS')";
            public final static String DELETE_PARTNERS = "hasRole('ROLE_DELETE_PARTNERS')";
            public final static String FILTER_PARTNERS = "hasRole('ROLE_FILTER_PARTNERS')";
        }

        public static class Model {
            public final static String REGISTER_MODELS = "hasRole('ROLE_REGISTER_MODELS')";
            public final static String UPDATE_MODELS = "hasRole('ROLE_UPDATE_MODELS')";
            public final static String DELETE_MODELS = "hasRole('ROLE_DELETE_MODELS')";
            public final static String FILTER_MODELS = "hasRole('ROLE_FILTER_MODELS')";
        }

        public static class Brand {
            public final static String REGISTER_BRANDS = "hasRole('ROLE_REGISTER_BRANDS')";
            public final static String UPDATE_BRANDS = "hasRole('ROLE_UPDATE_BRANDS')";
            public final static String DELETE_BRANDS = "hasRole('ROLE_DELETE_BRANDS')";
            public final static String FILTER_BRANDS = "hasRole('ROLE_FILTER_BRANDS')";
            public final static String UPLOAD_BRANDS = "hasRole('ROLE_UPLOAD_BRANDS')";
        }

        public static class Category {
            public final static String REGISTER_CATEGORIES = "hasRole('ROLE_REGISTER_CATEGORIES')";
            public final static String UPDATE_CATEGORIES = "hasRole('ROLE_UPDATE_CATEGORIES')";
            public final static String DELETE_CATEGORIES = "hasRole('ROLE_DELETE_CATEGORIES')";
            public final static String FILTER_CATEGORIES = "hasRole('ROLE_FILTER_CATEGORIES')";
        }

        public static class Card {
            public final static String REGISTER_CARDS = "hasRole('ROLE_FILTER_CARDS')";
            public final static String UPDATE_CARDS = "hasRole('ROLE_FILTER_CARDS')";
            public final static String DELETE_CARDS = "hasRole('ROLE_FILTER_CARDS')";
            public final static String FILTER_CARDS = "hasRole('ROLE_FILTER_CARDS')";
            public final static String SEARCH_CARDS_ICONS = "hasRole('ROLE_SEARCH_CARDS_ICONS')";
        }

        public static class Account {
            public final static String REGISTER_ACCOUNTS = "hasRole('ROLE_REGISTER_ACCOUNTS')";
            public final static String UPDATE_ACCOUNTS = "hasRole('ROLE_UPDATE_ACCOUNTS')";
            public final static String DELETE_ACCOUNTS = "hasRole('ROLE_DELETE_ACCOUNTS')";
            public final static String FILTER_ACCOUNTS = "hasRole('ROLE_FILTER_ACCOUNTS')";
            public final static String SEARCH_ACCOUNTS_ICONS = "hasRole('ROLE_SEARCH_ACCOUNTS_ICONS')";
        }

        public static class Transaction {
            public final static String REGISTER_TRANSACTIONS = "hasRole('ROLE_REGISTER_TRANSACTIONS')";
            public final static String UPDATE_TRANSACTIONS = "hasRole('ROLE_UPDATE_TRANSACTIONS')";
            public final static String DELETE_TRANSACTIONS = "hasRole('ROLE_DELETE_TRANSACTIONS')";
            public final static String PAYMENT_TRANSACTIONS = "hasRole('ROLE_PAYMENT_TRANSACTIONS')";
            public final static String REFUND_TRANSACTIONS = "hasRole('ROLE_REFUND_TRANSACTIONS')";
            public final static String FILTER_TRANSACTIONS = "hasRole('ROLE_FILTER_TRANSACTIONS')";
        }

        public static class Maintenance {
            public final static String REGISTER_MAINTENANCE = "hasRole('ROLE_REGISTER_MAINTENANCE')";
            public final static String UPDATE_MAINTENANCE = "hasRole('ROLE_UPDATE_MAINTENANCE')";
            public final static String DELETE_MAINTENANCE = "hasRole('ROLE_DELETE_MAINTENANCE')";
            public final static String FILTER_MAINTENANCE = "hasRole('ROLE_FILTER_MAINTENANCE')";
        }

        public static class Vehicle {
            public final static String REGISTER_VEHICLES = "hasRole('ROLE_REGISTER_VEHICLES')";
            public final static String UPDATE_VEHICLES = "hasRole('ROLE_UPDATE_VEHICLES')";
            public final static String DELETE_VEHICLES = "hasRole('ROLE_DELETE_VEHICLES')";
            public final static String VIEW_FIPE_VEHICLES = "hasRole('ROLE_VIEW_FIPE_VEHICLES')";
            public final static String FILTER_VEHICLES = "hasRole('ROLE_FILTER_VEHICLES')";
        }

        public static class Contract {
            public final static String REGISTER_CONTRACTS = "hasRole('ROLE_REGISTER_CONTRACTS')";
            public final static String UPDATE_CONTRACTS = "hasRole('ROLE_UPDATE_CONTRACTS')";
            public final static String DELETE_CONTRACTS = "hasRole('ROLE_DELETE_CONTRACTS')";
            public final static String TERMINATION_CONTRACTS = "hasRole('ROLE_TERMINATION_CONTRACTS')";
            public final static String FILTER_CONTRACTS = "hasRole('ROLE_FILTER_CONTRACTS')";
        }

        public static class Client {
            public final static String REGISTER_CLIENTS = "hasRole('ROLE_REGISTER_CLIENTS')";
            public final static String UPDATE_CLIENTS = "hasRole('ROLE_UPDATE_CLIENTS')";
            public final static String DELETE_CLIENTS = "hasRole('ROLE_DELETE_CLIENTS')";
            public final static String FILTER_CLIENTS = "hasRole('ROLE_FILTER_CLIENTS')";
        }
    }
}
