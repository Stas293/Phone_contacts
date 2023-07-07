package com.projects.phone_contacts.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = ContactCreateValidator.class)
@Target({FIELD})
@Retention(RUNTIME)
public @interface UniqueContactName {
    String message() default "Contact name is already taken";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
