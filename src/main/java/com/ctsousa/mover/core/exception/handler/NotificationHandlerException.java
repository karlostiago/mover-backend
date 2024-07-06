package com.ctsousa.mover.core.exception.handler;

import com.ctsousa.mover.core.exception.builder.ErrorBuilder;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.exception.error.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.util.List;

@RestControllerAdvice
public class NotificationHandlerException extends ResponseEntityExceptionHandler {

    @ExceptionHandler({NotificationException.class})
    public ResponseEntity<List<Error>> handleNotificationException(NotificationException ex) {
        List<Error> errors = List.of((ErrorBuilder.builder()
                .withStatus(HttpStatus.BAD_REQUEST)
                .withMessage(ex.getMessage())
                .withSeverity(ex.getSeverity())
                .build()));
        return ResponseEntity.badRequest().body(errors);
    }
}