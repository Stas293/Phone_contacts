package com.projects.phone_contacts.validation;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application.validation")
@ConditionalOnProperty(prefix = "application.validation", name = "enabled", havingValue = "true")
public record ValidationKeys(
        String phoneApiKey,
        String emailApiKey
) {
}
