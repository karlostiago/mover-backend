package com.ctsousa.mover.core.exception.notification;


import com.ctsousa.mover.core.exception.severity.Severity;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class NotificationNotFoundException extends RuntimeException {
    private final Severity severity;

    public NotificationNotFoundException(Severity severity) {
        this.severity = severity;
    }

    public NotificationNotFoundException(final String message) {
        super(message);
        this.severity = Severity.INFO;
    }

    public NotificationNotFoundException(final String message, final Severity severity) {
        super(message);
        this.severity = severity;
    }
}
