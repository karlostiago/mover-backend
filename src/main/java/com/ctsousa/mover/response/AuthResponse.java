package com.ctsousa.mover.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String token;
    private Long expiration;
    private String username;

    public AuthResponse(String token, Date expiration, String username) {
        this.token = token;
        this.expiration = expiration.getTime();
        this.username = username;
    }
}
