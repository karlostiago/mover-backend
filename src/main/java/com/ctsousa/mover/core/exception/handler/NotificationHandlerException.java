package com.ctsousa.mover.core.exception.handler;

import com.ctsousa.mover.core.exception.builder.ErrorBuilder;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.exception.error.Error;
import com.ctsousa.mover.core.exception.notification.NotificationNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@RestControllerAdvice
public class NotificationHandlerException extends ResponseEntityExceptionHandler {

    @ExceptionHandler({NotificationException.class})
    public ResponseEntity<List<Error>> handleNotificationException(NotificationException ex) {
        List<Error> errors = List.of((ErrorBuilder.builder()
                .withStatus(HttpStatus.BAD_REQUEST)
                .withMessage(ex.getMessage())
                .withDetails(ex)
                .withSeverity(ex.getSeverity())
                .build()));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler({NotificationNotFoundException.class})
    public ResponseEntity<List<Error>> handleNotificationNotFoundException(NotificationNotFoundException ex) {
        List<Error> errors = List.of((ErrorBuilder.builder()
                .withStatus(HttpStatus.NOT_FOUND)
                .withMessage(ex.getMessage())
                .withSeverity(ex.getSeverity())
                .withDetails(ex)
                .build()));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler({SQLIntegrityConstraintViolationException.class})
    public ResponseEntity<List<Error>> handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException ex) {
        List<Error> errors = List.of((ErrorBuilder.builder()
                .withStatus(HttpStatus.CONFLICT)
                .withMessage("Conflict: " + ex.getMessage())
                .build()));
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errors);
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<List<Error>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        List<Error> errors = List.of((ErrorBuilder.builder()
                .withStatus(HttpStatus.CONFLICT)
                .withMessage("Conflict: " + ex.getMessage())
                .build()));
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errors);
    }
}