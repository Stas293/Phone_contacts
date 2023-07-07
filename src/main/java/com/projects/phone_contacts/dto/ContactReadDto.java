package com.projects.phone_contacts.dto;

import com.projects.phone_contacts.model.Contact;

import java.util.List;
/**
 * DTO for {@link Contact}
 */
public record ContactReadDto(
        String name,
        List<String> emails,
        List<String> phones) {
}
