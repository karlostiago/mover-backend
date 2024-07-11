package com.ctsousa.mover.core.exception.error;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class Error {

    @Setter
    private String message;

    @Setter
    private Throwable details;

    @Setter
    private HttpStatus status;

    @Setter
    private Integer code;

    @Setter
    private String severity;

    private final LocalDate date = LocalDate.now();

    private final LocalTime time = LocalTime.now();
}