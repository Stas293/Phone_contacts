package com.projects.phone_contacts.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ContactDeleteDto(
        @NotBlank
        @Pattern(regexp = "^[a-zA-Zа-яА-Я]+(\\s[a-zA-Zа-яА-Я]+){1,2}$", message = "Name must contain only letters and spaces")
        String name
) {
}
