package com.projects.phone_contacts.exceptions;

import org.springframework.validation.FieldError;

import java.util.List;

public class ValidationException extends RuntimeException {
    private final List<FieldError> errors;

    public ValidationException(List<FieldError> errors) {
        this.errors = errors;
    }

    public List<FieldError> getErrors() {
        return errors;
    }
}
