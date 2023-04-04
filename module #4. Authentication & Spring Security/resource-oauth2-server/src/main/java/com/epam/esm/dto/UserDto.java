package com.epam.esm.dto;

import com.epam.esm.dto.group.OnPersist;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class UserDto extends RepresentationModel<UserDto> {

    @JsonIgnore
    private long id;

    @Pattern(regexp = "[\\w\\s]{3,64}+", message = "40002")
    @NotEmpty(groups = OnPersist.class)
    private String name;

    @Email
    @NotEmpty(groups = OnPersist.class)
    private String email;
}
