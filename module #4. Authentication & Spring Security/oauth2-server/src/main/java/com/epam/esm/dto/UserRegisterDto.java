package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegisterDto {
    @Email
    private String email;
    @Pattern(regexp = "\\w*")
    private String firstname;
    @Pattern(regexp = "\\w*")
    private String lastname;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Pattern(regexp = "[\\w*]{8,}", message = "Should be at least 8 characters")
    private String password;
}
