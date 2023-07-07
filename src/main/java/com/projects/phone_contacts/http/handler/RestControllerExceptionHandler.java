package com.projects.phone_contacts.http.handler;

import com.projects.phone_contacts.exceptions.ContactDeleteException;
import com.projects.phone_contacts.exceptions.ContactUpdateException;
import com.projects.phone_contacts.exceptions.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice(basePackages = "com.projects.phone_contacts.http.rest")
public class RestControllerExceptionHandler {
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationException(ValidationException exception) {
        return exception.getErrors()
                .stream()
                .collect(HashMap::new,
                        (map, error) -> map.put(error.getField(), error.getDefaultMessage()),
                        HashMap::putAll);
    }

    @ExceptionHandler(ContactUpdateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleContactUpdateException(ContactUpdateException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(ContactDeleteException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleContactDeleteException(ContactDeleteException exception) {
        return exception.getMessage();
    }
}
