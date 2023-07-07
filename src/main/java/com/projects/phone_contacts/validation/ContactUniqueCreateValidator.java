package com.projects.phone_contacts.validation;

import com.projects.phone_contacts.dto.ContactCreateUpdateDto;
import com.projects.phone_contacts.mapper.ContactCreateMapper;
import com.projects.phone_contacts.model.Contact;
import com.projects.phone_contacts.repository.EmailRepository;
import com.projects.phone_contacts.repository.PhoneRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
@Slf4j
public class ContactUniqueCreateValidator implements Validator {
    private final ContactCreateMapper contactCreateMapper;
    private final EmailRepository emailRepository;
    private final PhoneRepository phoneRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return ContactCreateUpdateDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (errors.hasErrors()) {
            return;
        }
        log.info("Validating contact uniqueness");
        ContactCreateUpdateDto contactCreateUpdateDto = (ContactCreateUpdateDto) target;
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Contact contact = contactCreateMapper.map(contactCreateUpdateDto);
        validateUniquenessOfPhone(errors, name, contact);
        validateUniquenessOfEmail(errors, name, contact);
        log.info("Contact uniqueness validated");
    }

    private void validateUniquenessOfEmail(Errors errors, String name, Contact contact) {
        contact.getEmails().stream()
                .filter(email -> emailRepository.existsByMailAndUser(
                        email.getUsername(), email.getDomain(), name))
                .forEach(email -> {
                    String message = String.format("Email with username %s and domain %s already exists",
                            email.getUsername(), email.getDomain());
                    String field = String.format("emails[%d]", contact.getEmails().indexOf(email));
                    log.info("Email with username {} and domain {} already exists",
                            email.getUsername(), email.getDomain());
                    errors.rejectValue(field, "email.exists", message);
                });
    }

    private void validateUniquenessOfPhone(Errors errors, String name, Contact contact) {
        contact.getPhones().stream()
                .filter(phone -> phoneRepository.existsByNumberAndUser(
                        phone.getAreaCode(), phone.getCountryCode(), phone.getNumber(), name))
                .forEach(phone -> {
                    String message = String.format("Phone number with country code %s, area code %s and number %s already exists",
                            phone.getCountryCode(), phone.getAreaCode(), phone.getNumber());
                    String field = String.format("phones[%d]", contact.getPhones().indexOf(phone));
                    log.info("Phone number with country code {}, area code {} and number {} already exists",
                            phone.getCountryCode(), phone.getAreaCode(), phone.getNumber());
                    errors.rejectValue(field, "phone.exists", message);
                });
    }
}
