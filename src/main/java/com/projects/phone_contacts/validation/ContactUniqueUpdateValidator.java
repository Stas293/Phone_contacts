package com.projects.phone_contacts.validation;

import com.projects.phone_contacts.dto.ContactCreateUpdateDto;
import com.projects.phone_contacts.mapper.ContactCreateMapper;
import com.projects.phone_contacts.model.Contact;
import com.projects.phone_contacts.model.Email;
import com.projects.phone_contacts.model.Phone;
import com.projects.phone_contacts.model.User;
import com.projects.phone_contacts.repository.ContactRepository;
import com.projects.phone_contacts.repository.EmailRepository;
import com.projects.phone_contacts.repository.PhoneRepository;
import com.projects.phone_contacts.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ContactUniqueUpdateValidator implements Validator {
    private final PhoneRepository phoneRepository;
    private final EmailRepository emailRepository;
    private final UserRepository userRepository;
    private final ContactCreateMapper contactCreateMapper;
    private final ContactRepository contactRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return ContactCreateUpdateDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (errors.hasErrors()) {
            return;
        }
        ContactCreateUpdateDto contactCreateUpdateDto = (ContactCreateUpdateDto) target;
        log.info("Validating contact update uniqueness");
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(name).orElseThrow();
        Contact contact = contactCreateMapper.map(contactCreateUpdateDto);
        contact.setUser(userRepository.findByUsername(name).orElseThrow());
        Optional<Contact> optionalContact = getContact(user, contact);
        if (optionalContact.isEmpty()) {
            errors.rejectValue("Name", "contact.not.found", "Contact not found");
        }
        if (errors.hasErrors()) {
            return;
        }
        validateContactsByPhone(errors, user, contact, optionalContact);
        validateContactsByEmail(errors, user, contact, optionalContact);
    }

    private void validateContactsByEmail(Errors errors, User user, Contact contact, Optional<Contact> optionalContact) {
        Map<Email, Contact> contactsByEmail = emailRepository.findAllByContact_User(user).stream()
                .collect(HashMap::new, (map, email) -> map.put(email, email.getContact()), Map::putAll);
        contact.getEmails().stream()
                .filter(email -> contactsByEmail.get(email) != null && !contactsByEmail.get(email).equals(optionalContact.get()))
                .forEach(email -> {
                    String message = String.format("Email with username %s and domain %s already exists",
                            email.getUsername(), email.getDomain());
                    String field = String.format("emails[%d]", contact.getEmails().indexOf(email));
                    errors.rejectValue(field, "email.exists", message);
                });
    }

    private void validateContactsByPhone(Errors errors, User user, Contact contact, Optional<Contact> optionalContact) {
        Map<Phone, Contact> contactsByPhone = phoneRepository.findAllByContact_User(user).stream()
                .collect(HashMap::new, (map, phone) -> map.put(phone, phone.getContact()), Map::putAll);
        contact.getPhones().stream()
                .filter(phone -> contactsByPhone.get(phone) != null && !contactsByPhone.get(phone).equals(optionalContact.get()))
                .forEach(phone -> {
                    String message = String.format("Phone number with country code %s, area code %s and number %s already exists",
                            phone.getCountryCode(), phone.getAreaCode(), phone.getNumber());
                    String field = String.format("phones[%d]", contact.getPhones().indexOf(phone));
                    errors.rejectValue(field, "phone.exists", message);
                });
    }

    private Optional<Contact> getContact(User user, Contact contact) {
        Optional<Contact> optionalContact;
        if (contact.getMiddleName() == null) {
            optionalContact = contactRepository.findContact(contact.getFirstName(), contact.getLastName(), user.getId());
        } else {
            optionalContact = contactRepository.findContact(contact.getFirstName(), contact.getMiddleName(), contact.getLastName(), user.getId());
        }
        return optionalContact;
    }
}
