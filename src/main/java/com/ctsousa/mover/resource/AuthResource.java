package com.ctsousa.mover.resource;

import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.token.JwtToken;
import com.ctsousa.mover.core.token.Token;
import com.ctsousa.mover.request.AuthRequest;
import com.ctsousa.mover.response.AuthResponse;
import com.ctsousa.mover.service.CustomUserDetailService;
import com.ctsousa.mover.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthResource {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final CustomUserDetailService customUserDetailService;

    @Value("${mover.secret-key}")
    private String secretKey;

    public AuthResource(AuthenticationManager authenticationManager, UserService userService, CustomUserDetailService customUserDetailService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.customUserDetailService = customUserDetailService;
    }

    @PostMapping(value = "/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthRequest request) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (AuthenticationException e) {
            throw new NotificationException(e.getMessage());
        }

        JwtToken jwtToken = new JwtToken(secretKey, customUserDetailService);
        Token token = jwtToken.generateToken(request.getUsername());
        return ResponseEntity.ok(new AuthResponse(token.getToken(), token.getExpiration(), userService.getUser().getName()));
    }
}
