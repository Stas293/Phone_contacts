package com.projects.phone_contacts.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = UserCreateValidator.class)
@Target({FIELD})
@Retention(RUNTIME)
public @interface UniqueUsername {
    String message() default "Login is already taken";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
