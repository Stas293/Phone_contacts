package com.projects.phone_contacts.config;

import com.projects.phone_contacts.security.jwt.JwtConfig;
import com.projects.phone_contacts.utility.ImageConfiguration;
import com.projects.phone_contacts.validation.ValidationKeys;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties({JwtConfig.class, ValidationKeys.class, ImageConfiguration.class})
@Configuration
public class ApplicationConfig {
}