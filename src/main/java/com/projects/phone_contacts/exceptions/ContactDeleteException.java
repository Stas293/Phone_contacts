package com.projects.phone_contacts.exceptions;

public class ContactDeleteException extends RuntimeException {
    public ContactDeleteException(String message) {
        super(message);
    }

    public ContactDeleteException(String message, Throwable cause) {
        super(message, cause);
    }

    public ContactDeleteException(Throwable cause) {
        super(cause);
    }
}
