package com.projects.phone_contacts.security.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

import static com.projects.phone_contacts.security.jwt.JWTUtils.getBase64EncodedSecretKey;

@ConfigurationProperties(prefix = "application.jwt")
public record JwtConfig(
        String secret,
        String header,
        String prefix,
        Integer expiration
) {
    @Override
    public String secret() {
        return getBase64EncodedSecretKey(secret);
    }

    @Override
    public String prefix() {
        return String.format("%s ", prefix);
    }
}
