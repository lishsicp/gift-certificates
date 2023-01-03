package com.epam.esm.entity;

import javax.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Tag implements Entity {

  private long id;

  @Pattern(regexp = "[\\w\\s]{2,128}+", message = "should be between 2 and 128 letters.")
  private String name;

}
