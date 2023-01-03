package com.epam.esm.entity.filter;

import com.epam.esm.entity.filter.annotation.EnumValue;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

@Data
@Validated
public class SearchFilter {
    @Pattern(regexp = "[\\w\\s]{2,128}+", message = "tag name should be between 2 and 128 letters.")
    String tagName;
    @Pattern(regexp = "[\\w\\s]{2,128}+", message = "Gift Certificate name should be between 2 and 128 letters.")
    String name;
    @Pattern(regexp = "[\\w\\s]{2,512}+", message = "Gift Certificate description should be between 2 and 128 letters.")
    String description;
    @Valid
    @EnumValue(enumClass = OrderBy.class, message = "Can only be equal to 'NAME' or 'LAST_UPDATE_DATE'")
    OrderBy orderBy;
    @Valid
    @EnumValue(enumClass = OrderByType.class, message = "Can only be equal to 'ASC' or 'DESC'")
    OrderByType orderByType;
}
