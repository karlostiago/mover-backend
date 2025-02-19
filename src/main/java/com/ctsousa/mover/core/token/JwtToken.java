package com.ctsousa.mover.core.token;

import com.ctsousa.mover.service.CustomUserDetailService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.List;

public class JwtToken {

    private static String SECRET_KEY;

    private final CustomUserDetailService customUserDetailService;

    public JwtToken(final String secretKey, CustomUserDetailService customUserDetailService) {
        JwtToken.SECRET_KEY = secretKey;
        this.customUserDetailService = customUserDetailService;
    }

    public Token generateToken(final String username) {
        Date expiresInOneHour = expiresInOneHour();
        return new Token(Jwts.builder()
                .setSubject(username)
                .claim("permissions", getPermissions(username))
                .claim("login", username)
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
        return customUserDetailService.getPermissions(username);
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
