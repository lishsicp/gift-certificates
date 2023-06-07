package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScopesDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 5478421047826070739L;

    @NotNull
    @JsonProperty("scopes")
    @Pattern(regexp = "[a-zA-Z.:\\s]+",
        message = "Please, provide space separated scopes. Example: 'user user:read user.email'")
    private String scopes;
}
