package com.projects.phone_contacts.dto;

import com.projects.phone_contacts.model.Email;
import com.projects.phone_contacts.validation.ExistsEmail;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link Email}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailCreateUpdateDto {
    @NotBlank
    @jakarta.validation.constraints.Email
    @Pattern(regexp = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", message = "Email must be valid")
    @ExistsEmail
    private String email;
}
