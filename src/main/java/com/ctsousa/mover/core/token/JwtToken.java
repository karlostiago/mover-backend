package com.ctsousa.mover.core.token;

import com.ctsousa.mover.core.entity.UserEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.enumeration.Functionality;
import com.ctsousa.mover.repository.PermissionRepository;
import com.ctsousa.mover.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

public class JwtToken {

    private static String SECRET_KEY;

    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;

    public JwtToken(final String secretKey, PermissionRepository permissionRepository, UserRepository userRepository) {
        JwtToken.SECRET_KEY = secretKey;
        this.permissionRepository = permissionRepository;
        this.userRepository = userRepository;
    }

    public Token generateToken(final String username) {
        Date expiresInOneHour = expiresInOneHour();
        return new Token(Jwts.builder()
                .setSubject(username)
                .claim("permissions", getPermissions(username))
                .setIssuedAt(new Date())
                .setExpiration(expiresInOneHour)
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), SignatureAlgorithm.HS256)
                .compact(), expiresInOneHour);
    }

    public static String extractUsername(final String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public static boolean validateToken(final String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equalsIgnoreCase(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private List<String> getPermissions(final String username) {
        List<String> permissions;
        if ("mover@sistemas.com".equalsIgnoreCase(username)) {
            permissions = Stream.of(Functionality.values())
                    .map(p -> "ROLE_" + p.name())
                    .toList();
        } else {
            UserEntity entity = userRepository.findByLogin(username)
                    .orElseThrow(() -> new NotificationException("Usuário não encontrado."));

            permissions = permissionRepository.findByUser(entity.getId())
                    .stream().map(p -> "ROLE_" + p)
                    .toList();
        }
        return permissions;
    }

    private static boolean isTokenExpired(final String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration().before(new Date());
    }

    private static Date expiresInOneHour() {
        return new Date(System.currentTimeMillis() + 1000 * 60 * 60);
    }
}
