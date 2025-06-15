package com.ctsousa.mover.core.util;

public final class CorsUtil {

    private CorsUtil() { }

    public static final String [] ALLOWED_ORIGINS = {
            "http://localhost:4200",
            "https://localhost:4200",
            "https://moverfrotas.netlify.app",
            "http://moverfrotas.netlify.app",
            "https://moverfrotashom.netlify.app",
            "http://moverfrotashom.netlify.app",
            "https://moverfrota.com.br",
            "http://moverfrota.com.br"
    };
}
