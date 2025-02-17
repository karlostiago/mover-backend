package com.ctsousa.mover.resource;

import com.ctsousa.mover.core.entity.UserEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.token.JwtToken;
import com.ctsousa.mover.core.token.Token;
import com.ctsousa.mover.repository.PermissionRepository;
import com.ctsousa.mover.repository.UserRepository;
import com.ctsousa.mover.request.AuthRequest;
import com.ctsousa.mover.response.AuthResponse;
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
    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Value("${mover.secret-key}")
    private String secretKey;

    public AuthResource(AuthenticationManager authenticationManager, UserRepository userRepository, PermissionRepository permissionRepository, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.permissionRepository = permissionRepository;
        this.userService = userService;
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

        JwtToken jwtToken = new JwtToken(secretKey, permissionRepository, userRepository);
        Token token = jwtToken.generateToken(request.getUsername());
        UserEntity entity = userService.findByLogin(request.getUsername());
        return ResponseEntity.ok(new AuthResponse(token.getToken(), token.getExpiration(), entity.getName()));
    }
}
