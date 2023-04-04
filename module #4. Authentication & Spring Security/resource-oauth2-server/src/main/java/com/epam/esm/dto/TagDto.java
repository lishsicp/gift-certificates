package com.epam.esm.dto;

import com.epam.esm.dto.group.OnPersist;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class TagDto extends RepresentationModel<TagDto> {

    @JsonIgnore
    private long id;

    @Pattern(regexp = "[\\w\\s]{3,64}+", message = "40002")
    @NotEmpty(groups = OnPersist.class)
    private String name;
}
