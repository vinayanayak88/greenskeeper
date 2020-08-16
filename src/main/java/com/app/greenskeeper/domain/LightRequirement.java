package com.app.greenskeeper.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public enum LightRequirement {

  BRIGHTLIGHT("Bright Light"),
  INDIRECTLIGHT("Indirect Light"),
  LOWLIGHT("Low Light");

  @NonNull
  private final String lightLevel;

}
