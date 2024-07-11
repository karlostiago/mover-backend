package com.ctsousa.mover.domain;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class Sender {
    private Long id;

    private Long clientId;

    private String email;

    private String code;

    private LocalDateTime expiryDate;
}
