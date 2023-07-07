package com.projects.phone_contacts.dto;

import com.projects.phone_contacts.validation.UniqueUsername;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserCreateDTO(
        @NotBlank(message = "Login is mandatory")
        @Size(min = 3, max = 255, message = "Login must be between 3 and 255 characters long")
        @UniqueUsername
        @Pattern(regexp = "^[A-Z][a-z0-9]+$", message = "Login must start with a capital letter and contain only letters and numbers")
        String login,
        @NotBlank(message = "Password is mandatory")
        @Size(min = 8, max = 255, message = "Password must be between 8 and 255 characters long")
        String password
) {
}
