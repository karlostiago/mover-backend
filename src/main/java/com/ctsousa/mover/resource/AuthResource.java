package com.ctsousa.mover.resource;

import com.ctsousa.mover.core.JwtToken;
import com.ctsousa.mover.request.AuthRequest;
import com.ctsousa.mover.response.AuthResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthResource {

    private final AuthenticationManager authenticationManager;

    @Value("${mover.secret-key}")
    private String secretKey;

    public AuthResource(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping(value = "/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        JwtToken jwtToken = new JwtToken(secretKey);
        String token = jwtToken.generateToken(request.getUsername());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
