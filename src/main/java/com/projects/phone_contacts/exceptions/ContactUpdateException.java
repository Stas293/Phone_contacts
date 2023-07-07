package com.projects.phone_contacts.exceptions;

public class ContactUpdateException extends RuntimeException {
    public ContactUpdateException(String message) {
        super(message);
    }

    public ContactUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public ContactUpdateException(Throwable cause) {
        super(cause);
    }
}
