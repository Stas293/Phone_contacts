package com.projects.phone_contacts.integration.validation;

import com.projects.phone_contacts.integration.IntegrationTestBase;
import com.projects.phone_contacts.integration.annotation.IT;
import com.projects.phone_contacts.model.Contact;
import com.projects.phone_contacts.repository.ContactRepository;
import com.projects.phone_contacts.validation.ContactCreateValidator;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@IT
@RequiredArgsConstructor
class ContactCreateValidatorTest extends IntegrationTestBase {
    private final ContactCreateValidator contactCreateValidator;
    private final ContactRepository contactRepository;

    @Test
    void testIsValid_UniqueContactName() {
        // Create a contact with a unique name
        Contact contact = new Contact();
        contact.setFirstName("John");
        contact.setLastName("Doe");
        contactRepository.save(contact);

        boolean johnDavidDoe = contactCreateValidator.isValid("John David Doe", null);

        Assertions.assertTrue(johnDavidDoe);
    }

    @Test
    void testIsValid_NonUniqueContactName() throws Exception {
        // Create a contact with a non-unique name
        Contact contact = new Contact();
        contact.setFirstName("John");
        contact.setLastName("Doe");
        contactRepository.save(contact);

        boolean johnDoe = contactCreateValidator.isValid("John Doe", null);

        Assertions.assertFalse(johnDoe);
    }
}