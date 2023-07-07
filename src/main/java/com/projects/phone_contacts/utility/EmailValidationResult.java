package com.projects.phone_contacts.utility;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmailValidationResult {
    @JsonProperty("format_valid")
    private boolean formatValid;
    private double score;
}
