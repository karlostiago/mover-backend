package com.ctsousa.mover.core.token;

import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.service.CustomUserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final CustomUserDetailService customUserDetailService;

    public JwtFilter(@Lazy CustomUserDetailService customUserDetailService) {
        this.customUserDetailService = customUserDetailService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws NotificationException, ServletException, IOException {

        Optional<String> tokenOpt = extractToken(request);

        if (tokenOpt.isPresent()) {
            var token = tokenOpt.get();
            try {
                processAuthentication(request, token);
            } catch (NotificationException e) {
                if (isSessionExpired(e)) {
                    handleSessionExpired(response);
                    return;
                }
                if (isServerRestarted(e)) {
                    handleServerRestarted(response);
                    return;
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private Optional<String> extractToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer")) {
            return Optional.of(authorizationHeader.substring(7));
        }
        return Optional.empty();
    }

    private void processAuthentication(HttpServletRequest request, String token) {
        var username = JwtToken.extractUsername(token);
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = customUserDetailService.loadUserByUsername(username);

            if (JwtToken.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
    }

    private boolean isSessionExpired(NotificationException e) {
        return "Sua sessão foi expirada.".equalsIgnoreCase(e.getMessage());
    }

    private boolean isServerRestarted(NotificationException e) {
        return "Secret key is null, login to recover automatically.".equalsIgnoreCase(e.getMessage());
    }

    private void handleSessionExpired(HttpServletResponse response) throws IOException {
        String message = "Sua sessão foi encerrada. Por motivos de segurança, você precisa fazer login novamente para continua.";
        response.setStatus(498);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }

    private void handleServerRestarted(HttpServletResponse response) throws IOException {
        String message = "O servidor foi reiniciado recentemente e sua sessão foi encerrada por motivos de segurança. Você será redirecionado para a tela de login para continuar.";
        response.setStatus(503);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}
