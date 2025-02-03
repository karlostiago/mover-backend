package com.ctsousa.mover.core.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

public final class JwtUtil {

    private static final String SECRET_KEY = "2e8d2c1fe8ff758f0427887984fa7ab501265633f51a3fc77874df667d433266"; //"mover sistemas de frotas";

    private JwtUtil() { }

    public static String generateToken(final String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expiresInOneHour())
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()), SignatureAlgorithm.HS256)
                .compact();
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
