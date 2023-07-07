package com.projects.phone_contacts.dto;

import com.projects.phone_contacts.model.Phone;
import com.projects.phone_contacts.validation.CreateContactValidationGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link Phone}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhoneCreateDto {
    @NotBlank
    @Pattern(regexp = "^\\+?\\d{12}$", message = "Phone number must be 12 digits long and start with a '+' sign. Example: +380939333333")
    @UniquePhone(groups = {CreateContactValidationGroup.class})
    private String number;
}
