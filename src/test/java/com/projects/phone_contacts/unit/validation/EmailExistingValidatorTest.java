package com.projects.phone_contacts.unit.validation;

import com.projects.phone_contacts.utility.EmailValidationResult;
import com.projects.phone_contacts.validation.EmailExistingValidator;
import com.projects.phone_contacts.validation.ValidationKeys;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailExistingValidatorTest {

    @Mock
    private ValidationKeys validationKeys;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ResponseEntity<EmailValidationResult> responseEntity;

    @InjectMocks
    private EmailExistingValidator emailExistingValidator;

    @Test
    void testIsValid_EmailValid() {
        // Mock the validationKeys
        when(validationKeys.emailApiKey()).thenReturn("your-api-key");

        // Mock the RestTemplate and responseEntity
        EmailValidationResult validationResult = new EmailValidationResult();
        validationResult.setFormatValid(true);
        when(responseEntity.getBody()).thenReturn(validationResult);
        when(restTemplate.getForEntity(anyString(), any())).thenReturn((ResponseEntity) responseEntity);

        // Test a valid email
        boolean isValid = emailExistingValidator.isValid("test@example.com", mock(ConstraintValidatorContext.class));
        assertTrue(isValid);

        // Verify that the RestTemplate was called with the correct URL
        verify(restTemplate).getForEntity("http://apilayer.net/api/check?access_key=your-api-key&email=test@example.com&smtp=1&format=1", EmailValidationResult.class);
    }

    @Test
    void testIsValid_EmailInvalid() {
        // Mock the validationKeys
        when(validationKeys.emailApiKey()).thenReturn("your-api-key");

        // Mock the RestTemplate and responseEntity
        EmailValidationResult validationResult = new EmailValidationResult();
        validationResult.setFormatValid(false);
        when(responseEntity.getBody()).thenReturn(validationResult);
        when(restTemplate.getForEntity(anyString(), any())).thenReturn((ResponseEntity) responseEntity);

        // Test an invalid email
        boolean isValid = emailExistingValidator.isValid("test@example.com", mock(ConstraintValidatorContext.class));
        assertFalse(isValid);

        // Verify that the RestTemplate was called with the correct URL
        verify(restTemplate).getForEntity("http://apilayer.net/api/check?access_key=your-api-key&email=test@example.com&smtp=1&format=1", EmailValidationResult.class);
    }
}