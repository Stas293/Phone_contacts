package com.projects.phone_contacts.dto;

import com.projects.phone_contacts.model.Contact;
import com.projects.phone_contacts.validation.UniqueContactName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * DTO for {@link Contact}
 */
public record ContactCreateDto(
        @NotBlank
        @Size(min = 2, max = 255)
        @UniqueContactName
        @Pattern(regexp = "^[a-zA-Zа-яА-Я]+(\\s[a-zA-Zа-яА-Я]+){1,2}$", message = "Name must contain only letters and spaces")
        String name,
        @Size(min = 1)
        List<EmailCreateUpdateDto> emails,
        @Size(min = 1)
        List<PhoneCreateDto> phones) {
}