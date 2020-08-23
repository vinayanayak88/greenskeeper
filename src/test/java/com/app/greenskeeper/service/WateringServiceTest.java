package com.app.greenskeeper.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.app.greenskeeper.domain.LightRequirement;
import com.app.greenskeeper.domain.Watering;
import com.app.greenskeeper.domain.WateringDuration;
import com.app.greenskeeper.entity.CategoryDetails;
import com.app.greenskeeper.entity.PlantDetails;
import com.app.greenskeeper.entity.WateringDetails;
import com.app.greenskeeper.repository.PlantRepository;
import com.app.greenskeeper.repository.WateringRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WateringServiceTest {

  @Mock
  private WateringRepository wateringRepository;
  @Mock
  private PlantRepository plantRepository;
  @InjectMocks
  private WateringService wateringService;

  @Test
  @Ignore
  public void testWaterNow() {
    UUID plantId = UUID.randomUUID();
    PlantDetails plantDetails = PlantDetails.builder()
                                            .id(UUID.fromString(
                                                "caa46f55-3a2a-4d33-80e9-9b3dc1a88ad9"))
                                            .name("Peace Lilly")
                                            .categoryDetails(CategoryDetails.builder()
                                                                            .name("Foliage Plant")
                                                                            .duration(
                                                                                WateringDuration.WEEKLY)
                                                                            .wateringPeriod(2)
                                                                            .lightRequirement(
                                                                                LightRequirement.INDIRECTLIGHT)
                                                                            .build())
                                            .build();
    given(plantRepository.findById(plantId)).willReturn(Optional.of(buildPlantDetails()));
    LocalDateTime lastWateredOn = LocalDateTime.now();
    WateringDetails wateringDetails = WateringDetails.builder()
                                                     .id(UUID.fromString(
                                                         "ba46f55-3a2a-4d33-80e9-9b3dc1a88ad9"))
                                                     .lastWateredOn(lastWateredOn)
                                                     .nextWateringDay(lastWateredOn.plusDays(3))
                                                     .build();
    Watering watering = wateringService.waterNow(plantId);

    assertThat(watering.getLastWateredOn()).isEqualTo(lastWateredOn);

  }

  private PlantDetails buildPlantDetails() {
    return PlantDetails.builder()
                       .name("Peace Lilly")
                       .categoryDetails(buildCategoryDetails())
                       .build();
  }

  private CategoryDetails buildCategoryDetails() {
    return CategoryDetails.builder()
                          .name("Foliage Plant")
                          .duration(WateringDuration.WEEKLY)
                          .wateringPeriod(2)
                          .lightRequirement(LightRequirement.INDIRECTLIGHT)
                          .build();
  }


}
