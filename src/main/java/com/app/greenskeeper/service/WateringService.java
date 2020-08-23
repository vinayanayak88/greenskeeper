package com.app.greenskeeper.service;

import com.app.greenskeeper.domain.Watering;
import com.app.greenskeeper.entity.PlantDetails;
import com.app.greenskeeper.entity.WateringDetails;
import com.app.greenskeeper.exception.PlantNotFoundException;
import com.app.greenskeeper.repository.PlantRepository;
import com.app.greenskeeper.repository.WateringRepository;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import java.time.LocalDateTime;
import java.util.Optional;
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
  public Watering waterNow(UUID plantId) {
    PlantDetails plantDetails = plantRepository.findById(plantId).orElseThrow(
        () -> new PlantNotFoundException(
            "I'm sorry, I couldn't find the plant. You probably might have moved me to the graveyard :( "));
    //Optional<WateringDetails> wateringDetails = wateringRepository.findByPlantId(plantDetails.getId());
    if(plantDetails.getWateringDetails() != null){
      updateWateringCondition(plantDetails);
    }
    return createWateringCondition(plantDetails);
//    return wateringDetails.map(details -> updateWateringCondition(plantDetails, details))
//                         .orElseGet(() -> createWateringCondition(plantDetails));
  }

  private Watering updateWateringCondition(PlantDetails plantDetails) {
    plantDetails.getWateringDetails().setLastWateredOn(LocalDateTime.now());
    plantDetails.getWateringDetails()
        .setNextWateringDay(calculateNextWateringDay(plantDetails,
                                                     plantDetails.getWateringDetails().getLastWateredOn()));
    wateringRepository.save(plantDetails.getWateringDetails());
    return buildWateringResponse(plantDetails.getWateringDetails());
  }

  private Watering createWateringCondition(PlantDetails plant) {
    WateringDetails wateringDetails = WateringDetails.builder()
                                                    .lastWateredOn(LocalDateTime.now())
                                                    .nextWateringDay(LocalDateTime.now().plusDays(
                                                      plant.getCategoryDetails().getWateringPeriod()))
                                                    .build();
    wateringRepository.save(wateringDetails);
    return buildWateringResponse(wateringDetails);
  }

  private LocalDateTime calculateNextWateringDay(PlantDetails plant, LocalDateTime lastWateredOn) {
    return lastWateredOn.plusDays(plant.getCategoryDetails().getWateringPeriod());
  }

  private Watering buildWateringResponse(WateringDetails wateringDetails){
    return Watering.builder()
            .id(wateringDetails.getId())
            .lastWateredOn(wateringDetails.getLastWateredOn())
            .nextWateringDay(wateringDetails.getNextWateringDay())
            .build();
  }
}
