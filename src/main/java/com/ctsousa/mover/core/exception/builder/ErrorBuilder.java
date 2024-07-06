package com.ctsousa.mover.core.exception.builder;

import com.ctsousa.mover.core.exception.error.Error;
import com.ctsousa.mover.core.exception.severity.Severity;
import org.springframework.http.HttpStatus;

public class ErrorBuilder {

    private static Error error;

    public ErrorBuilder() {
        error = new Error();
    }

    public static ErrorBuilder builder() {
        return new ErrorBuilder();
    }

    public ErrorBuilder withStatus(HttpStatus status) {
        error.setStatus(status);
        error.setCode(status.value());
        return this;
    }

    public ErrorBuilder withMessage(String message) {
        error.setMessage(message);
        return this;
    }

    public ErrorBuilder withDetails(Throwable ex) {
        error.setDetails(ex);
        return this;
    }

    public ErrorBuilder withSeverity(Severity severity) {
        error.setSeverity(severity.name());
        return this;
    }

    public Error build() {
        return error;
    }

}