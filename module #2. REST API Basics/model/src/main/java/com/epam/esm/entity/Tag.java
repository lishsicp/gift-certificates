package com.epam.esm.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tag implements Entity {

  private long id;

  @NotEmpty
  @Pattern(regexp = "[\\w\\s]{2,128}+", message = "40002")
  private String name;

}
