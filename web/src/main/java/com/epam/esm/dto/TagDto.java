package com.epam.esm.dto;

import com.epam.esm.dto.group.OnPersist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TagDto extends RepresentationModel<TagDto> {

    @Min(value = 1, message = "40001")
    private Long id;

    @Pattern(regexp = "[\\w\\s]{3,64}+", message = "40002")
    @NotEmpty(groups = OnPersist.class)
    private String name;

}
