package com.ctsousa.mover.core.config;

import com.ctsousa.mover.core.util.CorsUtil;
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

        String origin = enabledOrigin(request);

        response.setHeader("Access-Control-Allow-Origin", origin);
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Content-Security-Policy", "frame-ancestors " + origin);

        if ("OPTIONS".equals(request.getMethod()) && isEnabledOrigin(request)) {
            response.setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, PUT, PATCH, OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, Accept");
            response.setHeader("Access-Control-Max-Age", "3600");

            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    private String enabledOrigin(final HttpServletRequest request) {
        String origemPermitida = null;

        for (String origin : CorsUtil.ALLOWED_ORIGINS) {
            if (request.getHeader(ORIGIN) != null && request.getHeader(ORIGIN).equalsIgnoreCase(origin.trim())) {
                origemPermitida = origin;
                break;
            }
        }
        return origemPermitida;
    }

    private boolean isEnabledOrigin(final HttpServletRequest request) {
        return enabledOrigin(request) != null;
    }
}
