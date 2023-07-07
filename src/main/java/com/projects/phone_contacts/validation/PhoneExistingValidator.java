package com.projects.phone_contacts.validation;

import com.projects.phone_contacts.utility.PhoneValidationResult;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class PhoneExistingValidator implements ConstraintValidator<ExistsPhone, String> {
    private final ValidationKeys validationKeys;
    private static final String URL = "http://apilayer.net/api/validate?number=%s&access_key=%s&format=1";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        String url = String.format(URL, value, validationKeys.phoneApiKey());
        ResponseEntity<PhoneValidationResult> response = new RestTemplate().getForEntity(url, PhoneValidationResult.class);
        PhoneValidationResult validationResult = response.getBody();
        return validationResult != null && validationResult.isValid();
    }
}
