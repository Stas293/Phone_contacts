package com.projects.phone_contacts.utility;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.image")
public record ImageConfiguration(
        String basePath
) {
}
