package com.ctsousa.mover.core.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.io.IOException;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsConfig implements Filter {

    private static final String ORIGIN = "Origin";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        response.setHeader("Access-Control-Allow-Origin", origemPermitida(request));
        response.setHeader("Access-Control-Allow-Credentials", "true");

        if ("OPTIONS".equals(request.getMethod()) && temOrigemPermitida(request)) {
            response.setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, PUT, PATCH, OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, Accept");
            response.setHeader("Access-Control-Max-Age", "3600");

            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    private String origemPermitida(final HttpServletRequest request) {
        String[] origens = getOrigensPermidas();
        String origemPermitida = "http://localhost:4200";

        for (String origem : origens) {
            if (request.getHeader(ORIGIN) != null && request.getHeader(ORIGIN).equals(origem.trim())) {
                origemPermitida = origem;
                break;
            }
        }

        return origemPermitida;
    }

    private boolean temOrigemPermitida(final HttpServletRequest request) {
        return origemPermitida(request) != null;
    }

    private String [] getOrigensPermidas() {
        return "http://localhost:4200, https://localhost:4200"
                .split(",");
    }
}
