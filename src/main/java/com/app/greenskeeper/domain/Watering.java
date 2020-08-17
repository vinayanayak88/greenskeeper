package com.app.greenskeeper.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
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
public class Watering {

  private UUID id;
  private UUID plantId;
  private LocalDateTime lastWateredOn;
  private LocalDateTime nextWateringDay;
  private List<WateringHistory> wateringHistories;

}