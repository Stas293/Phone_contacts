package com.projects.phone_contacts.dto;

public record JwtRequestDTO(
        String login,
        String password
) {
}
