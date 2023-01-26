package com.epam.esm.dto;

import com.epam.esm.dto.group.OnPersist;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
public class TagDto {

    @Min(value = 1, message = "40001")
    private Long id;

    @Pattern(regexp = "[\\w\\s]{3,64}+", message = "40002")
    @NotEmpty(groups = OnPersist.class)
    private String name;

}
