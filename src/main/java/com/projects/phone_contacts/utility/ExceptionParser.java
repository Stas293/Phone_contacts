package com.projects.phone_contacts.utility;

import com.projects.phone_contacts.exceptions.ValidationException;
import lombok.experimental.UtilityClass;
import org.springframework.validation.BindingResult;

@UtilityClass
public class ExceptionParser {
    public static void parseValidation(BindingResult bindingResult) {
        throw new ValidationException(bindingResult.getFieldErrors());
    }
}
