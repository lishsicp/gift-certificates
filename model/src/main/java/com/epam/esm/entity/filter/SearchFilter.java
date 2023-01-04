package com.epam.esm.entity.filter;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class SearchFilter {
    @Pattern(regexp = "[\\w\\s]{2,128}+", message = "tag name should be between 2 and 128 letters.")
    String tagName;
    @Pattern(regexp = "[\\w\\s]{2,128}+", message = "Gift Certificate name should be between 2 and 128 letters.")
    String name;
    @Pattern(regexp = "[\\w\\s]{2,512}+", message = "Gift Certificate description should be between 2 and 128 letters.")
    String description;
    @Pattern(regexp = "NAME|LAST_UPDATE_DATE", message = "Can only be equal to 'NAME' or 'LAST_UPDATE_DATE'")
    String orderBy;
    @Pattern(regexp = "ASC|DESC", message = "Can only be equal to 'ASC' or 'DESC'")
    String orderByType;
}
