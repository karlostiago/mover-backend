package com.ctsousa.mover.core.exception.notification;

import com.ctsousa.mover.core.exception.severity.Severity;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NotificationException extends RuntimeException {

    private final Severity severity;
    private final HttpStatus httpStatus;

    public NotificationException(final String message) {
        super(message);
        this.severity = Severity.ERROR;
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public NotificationException(final String message, final Severity severity) {
        super(message);
        this.severity = severity;
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public NotificationException(final String message, final Severity severity, HttpStatus httpStatus) {
        super(message);
        this.severity = severity;
        this.httpStatus = httpStatus;
    }
}