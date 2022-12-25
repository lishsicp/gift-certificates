package com.epam.esm.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class Tag implements Entity {

  @NonNull
  private long id;

  private String name;

}
