package com.app.greenskeeper.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class Category {

    private UUID id;
    private String title;
    private WateringDuration duration;
    private String frequency;
    private LightRequirement lightRequirement;
}
