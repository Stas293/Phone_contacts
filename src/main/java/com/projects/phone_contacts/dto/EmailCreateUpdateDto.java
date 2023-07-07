package com.projects.phone_contacts.dto;

import com.projects.phone_contacts.model.Email;
import com.projects.phone_contacts.validation.CreateContactValidationGroup;
import com.projects.phone_contacts.validation.UniqueEmail;
import com.projects.phone_contacts.validation.UpdateContactValidationGroup;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.sql.Update;

/**
 * DTO for {@link Email}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailCreateDto {
    @NotBlank
    @jakarta.validation.constraints.Email
    @UniqueEmail(groups = {CreateContactValidationGroup.class, UpdateContactValidationGroup.class})
    private String email;
}
