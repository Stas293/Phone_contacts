package com.projects.phone_contacts.validation;

import com.projects.phone_contacts.utility.EmailValidationResult;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class EmailExistingValidator implements ConstraintValidator<ExistsEmail, String> {
    private final ValidationKeys validationKeys;
    private static final String URL = "http://apilayer.net/api/check?access_key=%s&email=%s&smtp=1&format=1";
    private final RestTemplate restTemplate;

    @Autowired
    public EmailExistingValidator(ValidationKeys validationKeys) {
        this.validationKeys = validationKeys;
        this.restTemplate = new RestTemplate();
    }

    public EmailExistingValidator(ValidationKeys validationKeys, RestTemplate restTemplate) {
        this.validationKeys = validationKeys;
        this.restTemplate = restTemplate;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        String url = String.format(URL, validationKeys.emailApiKey(), value);
        ResponseEntity<EmailValidationResult> response = restTemplate.getForEntity(url, EmailValidationResult.class);
        EmailValidationResult validationResult = response.getBody();
        return validationResult != null && validationResult.isFormatValid();
    }
}
