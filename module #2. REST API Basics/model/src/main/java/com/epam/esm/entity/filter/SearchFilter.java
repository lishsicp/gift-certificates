package com.epam.esm.entity.filter;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
@Builder
public class SearchFilter {
    @Pattern(regexp = "[\\w\\s]{2,128}+", message = "40002")
    String tagName;
    @Pattern(regexp = "[\\w\\s]{2,128}+", message = "40003")
    String name;
    @Pattern(regexp = "[\\w\\s]{2,512}+", message = "40004")
    String description;
    @Pattern(regexp = "NAME|LAST_UPDATE_DATE", message = "40007")
    String sortBy;
    @Pattern(regexp = "ASC|DESC", message = "40008")
    String sortByType;
}
