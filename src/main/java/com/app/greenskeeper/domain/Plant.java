package com.app.greenskeeper.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Plant {

  private UUID id;
  private String name;
  private String category;
  private String wateringInterval;
  private WateringInformation wateringInformation;
  private List<WateringHistory> wateringHistories;

}
