package com.ctsousa.mover.core.exception.notification;

import com.ctsousa.mover.core.exception.severity.Severity;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class NotificationException extends RuntimeException {

    private final Severity severity;

    public NotificationException(final String message) {
        super(message);
        this.severity = Severity.ERROR;
    }

    public NotificationException(final String message, final Severity severity) {
        super(message);
        this.severity = severity;
    }
}