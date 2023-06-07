package com.epam.esm.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLoginDto {

    @Email
    @NotBlank
    private String email;
    @Pattern(regexp = "[\\w!\"#$%&'()*+,\\-./:;<=>?@\\[\\\\\\]^_`{|}~]{8,}", message = "Should be at least 8 characters")
    private String password;
}