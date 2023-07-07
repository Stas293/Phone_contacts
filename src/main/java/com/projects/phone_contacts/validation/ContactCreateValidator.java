package com.projects.phone_contacts.validation;

import com.projects.phone_contacts.repository.ContactRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ContactCreateValidator implements ConstraintValidator<UniqueContactName, String> {
    private final ContactRepository contactRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        log.info("Validating contact name: {}", value);
        String[] names = value.split(" ");
        String firstName = names[0];
        String middleName = names.length > 2 ? names[1] : null;
        String lastName = names.length > 2 ? names[2] : names[1];
        if (middleName == null) {
            return !contactRepository.existsByFirstNameAndLastName(firstName, lastName);
        } else {
            return !contactRepository.existsByFirstNameAndMiddleNameAndLastName(firstName, middleName, lastName);
        }
    }
}
