package com.projects.phone_contacts.unit.validation;

import com.projects.phone_contacts.repository.ContactRepository;
import com.projects.phone_contacts.validation.ContactCreateValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContactCreateValidatorTest {

    @Mock
    private ContactRepository contactRepository;

    @InjectMocks
    private ContactCreateValidator contactCreateValidator;

    @Test
    void testIsValid_UniqueContactName() {
        // Mock the ContactRepository
        when(contactRepository.existsByFirstNameAndLastName(anyString(), anyString())).thenReturn(false);

        // Test with a unique contact name
        boolean isValid = contactCreateValidator.isValid("John Doe", null);
        assertTrue(isValid);

        // Verify that the ContactRepository method was called
        verify(contactRepository).existsByFirstNameAndLastName("John", "Doe");
    }

    @Test
    void testIsValid_NonUniqueContactName() {
        // Mock the ContactRepository
        when(contactRepository.existsByFirstNameAndLastName(anyString(), anyString())).thenReturn(true);

        // Test with a non-unique contact name
        boolean isValid = contactCreateValidator.isValid("John Doe", null);
        assertFalse(isValid);

        // Verify that the ContactRepository method was called
        verify(contactRepository).existsByFirstNameAndLastName("John", "Doe");
    }

    @Test
    void testIsValid_UniqueContactNameWithMiddleName() {
        // Mock the ContactRepository
        when(contactRepository.existsByFirstNameAndMiddleNameAndLastName(anyString(), anyString(), anyString())).thenReturn(false);

        // Test with a unique contact name with a middle name
        boolean isValid = contactCreateValidator.isValid("John David Doe", null);
        assertTrue(isValid);

        // Verify that the ContactRepository method was called
        verify(contactRepository).existsByFirstNameAndMiddleNameAndLastName("John", "David", "Doe");
    }

    @Test
    void testIsValid_NonUniqueContactNameWithMiddleName() {
        // Mock the ContactRepository
        when(contactRepository.existsByFirstNameAndMiddleNameAndLastName(anyString(), anyString(), anyString())).thenReturn(true);

        // Test with a non-unique contact name with a middle name
        boolean isValid = contactCreateValidator.isValid("John David Doe", null);
        assertFalse(isValid);

        // Verify that the ContactRepository method was called
        verify(contactRepository).existsByFirstNameAndMiddleNameAndLastName("John", "David", "Doe");
    }
}

