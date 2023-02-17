package com.epam.esm.entity;

import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tag implements Entity {

  private long id;

  @Pattern(regexp = "[\\w\\s]{2,128}+", message = "40002")
  private String name;

}
