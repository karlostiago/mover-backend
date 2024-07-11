package com.ctsousa.mover.core.configuration;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.io.IOException;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class Cors implements Filter {

    private static final String ORIGIN = "Origin";
    private final String origin = "http://192.168.1.2:8081, https://192.168.1.2:8081";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        response.setHeader("Access-Control-Allow-Origin", enableOrigins(request));
        response.setHeader("Access-Control-Allow-Credentials", "true");

        if ("OPTIONS".equals(request.getMethod()) && isOriginOk(request)) {
            response.setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, PUT, PATCH, OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, Accept");
            response.setHeader("Access-Control-Max-Age", "3600");

            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    private String enableOrigins(final HttpServletRequest request) {
        String[] origens = origin.split(",");
        String origin = "http://192.168.1.2:8081";

        for (String origem : origens) {
            if (request.getHeader(ORIGIN) != null && request.getHeader(ORIGIN).equals(origem.trim())) {
                origin = origem;
                break;
            }
        }

        return origin;
    }

    private boolean isOriginOk(final HttpServletRequest request) {
        return enableOrigins(request) != null;
    }
}
