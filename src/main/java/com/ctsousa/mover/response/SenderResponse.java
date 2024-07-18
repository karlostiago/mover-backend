package com.ctsousa.mover.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SenderResponse {
    private Long id;

    private Long clientId;

    private String email;

    private String code;

    private LocalDateTime expiryDate;
}
