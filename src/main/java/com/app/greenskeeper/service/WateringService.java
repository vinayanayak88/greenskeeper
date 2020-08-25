package com.app.greenskeeper.service;

import com.app.greenskeeper.domain.Plant;
import com.app.greenskeeper.domain.WateringInformation;
import com.app.greenskeeper.entity.PlantDetails;
import com.app.greenskeeper.entity.WateringDetails;
import com.app.greenskeeper.entity.WateringHistoryDetails;
import com.app.greenskeeper.exception.PlantNotFoundException;
import com.app.greenskeeper.repository.PlantRepository;
import com.app.greenskeeper.repository.WateringRepository;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@GraphQLApi
@Service
@RequiredArgsConstructor
public class WateringService {

  @NonNull
  private WateringRepository wateringRepository;
  @NonNull
  private PlantRepository plantRepository;

  @GraphQLMutation(name = "waterPlant")
  public Plant waterNow(UUID plantId) {
    PlantDetails plantDetails = plantRepository.findById(plantId).orElseThrow(
        () -> new PlantNotFoundException(
            "I'm sorry, I couldn't find the plant. You probably might have moved me to the graveyard :( "));
    if (plantDetails.getWateringDetails() != null) {
      return updateWateringInformation(plantDetails);
    }
    return createWateringInformation(plantDetails);
  }

  private Plant updateWateringInformation(PlantDetails plantDetails) {
    WateringHistoryDetails wateringHistoryDetails = WateringHistoryDetails.builder()
                                                                          .wateringTime(plantDetails
                                                                                            .getWateringDetails()
                                                                                            .getLastWateredOn())
                                                                          .build();
    LocalDateTime lastWateredOn = LocalDateTime.now();
    WateringDetails wateringDetails = WateringDetails.builder()
                                                     .id(plantDetails.getWateringDetails().getId())
                                                     .lastWateredOn(lastWateredOn)
                                                     .nextWateringDay(
                                                         calculateNextWateringDay(plantDetails,
                                                                                  lastWateredOn))
                                                     .build();
    plantDetails.setWateringDetails(wateringDetails);
    plantDetails.setWateringHistoryDetails(Collections.singletonList(wateringHistoryDetails));

    plantRepository.save(plantDetails);
    return buildPlantResponse(plantDetails);
  }

  private Plant createWateringInformation(PlantDetails plantDetails) {
    LocalDateTime lastWateredOn = LocalDateTime.now();
    WateringDetails wateringDetails = WateringDetails.builder()
                                                     .lastWateredOn(lastWateredOn)
                                                     .nextWateringDay(lastWateredOn.plusDays(
                                                         plantDetails.getWateringInterval()))
                                                     .build();
    plantDetails.setWateringDetails(wateringDetails);
    plantRepository.save(plantDetails);
    return buildPlantResponse(plantDetails);
  }

  private LocalDateTime calculateNextWateringDay(PlantDetails plant, LocalDateTime lastWateredOn) {
    return lastWateredOn.plusDays(plant.getWateringInterval());
  }

  private Plant buildPlantResponse(PlantDetails plantDetails) {
    return Plant.builder()
                .id(plantDetails.getId())
                .name(plantDetails.getName())
                .category(plantDetails.getCategory())
                .wateringInterval(String.valueOf(plantDetails.getWateringInterval()))
                .wateringInformation(buildWateringResponse(plantDetails))
                .build();

  }

  private WateringInformation buildWateringResponse(PlantDetails plantDetails) {
    return WateringInformation.builder()
                              .id(plantDetails.getWateringDetails().getId())
                              .lastWateredOn(plantDetails.getWateringDetails().getLastWateredOn())
                              .nextWateringDay(
                                  plantDetails.getWateringDetails().getNextWateringDay())
                              .build();
  }
}
