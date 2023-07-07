package com.projects.phone_contacts.dto;

import com.projects.phone_contacts.model.Contact;
import com.projects.phone_contacts.validation.CreateContactValidationGroup;
import com.projects.phone_contacts.validation.UniqueContactName;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.Set;

/**
 * DTO for {@link Contact}
 */
@Builder
public record ContactCreateUpdateDto(
        @NotBlank
        @Size(min = 2, max = 255)
        @UniqueContactName(groups = CreateContactValidationGroup.class)
        @Pattern(regexp = "^[a-zA-Zа-яА-Я]+(\\s[a-zA-Zа-яА-Я]+){1,2}$", message = "Name must contain only letters and spaces")
        String name,
        @Size(min = 1)
        Set<@Valid EmailCreateUpdateDto> emails,
        @Size(min = 1)
        Set<@Valid PhoneCreateUpdateDto> phones) {
}