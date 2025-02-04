package com.ctsousa.mover.core.token;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class Token {

    private String token;
    private Date expiration;
}
