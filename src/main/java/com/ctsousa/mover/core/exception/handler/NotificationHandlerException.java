package com.ctsousa.mover.core.exception.handler;

import com.ctsousa.mover.core.exception.builder.ErrorBuilder;
import com.ctsousa.mover.core.exception.error.Error;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.exception.notification.NotificationNotFoundException;
import com.ctsousa.mover.core.exception.severity.Severity;
import lombok.NonNull;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class NotificationHandlerException extends ResponseEntityExceptionHandler {

    @ExceptionHandler({NotificationException.class})
    public ResponseEntity<List<Error>> handleNotificationException(NotificationException ex) {
        return ResponseEntity.status(ex.getHttpStatus()).body(getErrors(ex, ex.getSeverity(), ex.getHttpStatus()));
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<List<Error>> handleException(Exception ex) {
        if (ex instanceof AuthorizationDeniedException) {
            NotificationException e = new NotificationException(ex.getMessage(), Severity.ERROR, HttpStatus.FORBIDDEN);
            return handleNotificationException(e);
        }
        return ResponseEntity.internalServerError().body(getErrors(ex, Severity.ERROR, HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler({NotificationNotFoundException.class})
    public ResponseEntity<List<Error>> handleNotificationNotFoundException(NotificationNotFoundException ex) {
        return ResponseEntity.badRequest().body(getErrors(ex, ex.getSeverity(), HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler({SQLIntegrityConstraintViolationException.class})
    public ResponseEntity<List<Error>> handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(getConflictErrors(ex));
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<List<Error>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(getConflictErrors(ex));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, @NonNull HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        BindingResult bindingResult = ex.getBindingResult();
        List<Error> errors = new ArrayList<>(bindingResult.getFieldErrors().size());

        for (FieldError error : bindingResult.getFieldErrors()) {
            errors.add(ErrorBuilder.builder()
                    .withStatus(HttpStatus.BAD_REQUEST)
                    .withMessage(error.getDefaultMessage())
                    .build());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    private List<Error> getConflictErrors(Exception ex) {
        return List.of((ErrorBuilder.builder()
                .withStatus(HttpStatus.CONFLICT)
                .withMessage("Conflict: " + ex.getMessage())
                .build()));
    }

    private List<Error> getErrors(Exception ex, Severity severity, HttpStatus httpStatus) {
        return List.of((ErrorBuilder.builder()
                .withStatus(httpStatus)
                .withMessage(ex.getMessage())
                .withDetails(ex)
                .withSeverity(severity)
                .build()));
    }
}