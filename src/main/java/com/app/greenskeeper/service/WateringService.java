package com.app.greenskeeper.service;

import com.app.greenskeeper.domain.Watering;
import com.app.greenskeeper.entity.PlantEntity;
import com.app.greenskeeper.entity.WateringEntity;
import com.app.greenskeeper.exception.PlantNotFoundException;
import com.app.greenskeeper.repository.PlantRepository;
import com.app.greenskeeper.repository.WateringRepository;
import io.leangen.graphql.annotations.GraphQLQuery;
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

  @GraphQLQuery(name = "waterPlant")
  public void waterNow(UUID plantId) {
    PlantEntity plant = plantRepository.findById(plantId).orElseThrow(
        () -> new PlantNotFoundException(
            "I'm sorry, I couldn't find the plant. You probably might have moved me to the graveyard :( "));
    Optional<WateringEntity> wateringEntity = wateringRepository.findByPlantId(plant.getId());
    wateringEntity.ifPresent(entity -> updateWateringCondition(plant, entity));
    createWateringCondition(plant);
  }

  private void updateWateringCondition(PlantEntity plant,
                                       WateringEntity wateringEntity) {
    wateringEntity.setLastWateredOn(LocalDateTime.now());
    wateringEntity
        .setNextWateringDay(calculateNextWateringDay(plant, wateringEntity.getLastWateredOn()));
    wateringRepository.save(wateringEntity);
  }

  private void createWateringCondition(PlantEntity plant) {
    WateringEntity wateringEntity = WateringEntity.builder()
                                                  .plantId(plant.getId())
                                                  .lastWateredOn(LocalDateTime.now())
                                                  .nextWateringDay(LocalDateTime.now().plusDays(
                                                      plant.getCategory().getWateringPeriod()))
                                                  .build();
    wateringRepository.save(wateringEntity);
    //return buildWateringResponse(wateringEntity);
  }

  private LocalDateTime calculateNextWateringDay(PlantEntity plant, LocalDateTime lastWateredOn) {
    return lastWateredOn.plusDays(plant.getCategory().getWateringPeriod());
  }

  private Watering buildWateringResponse(WateringEntity wateringEntity){
    return Watering.builder()
            .id(wateringEntity.getId())
            .plantId(wateringEntity.getPlantId())
            .lastWateredOn(wateringEntity.getLastWateredOn())
            .nextWateringDay(wateringEntity.getNextWateringDay())
            .build();
  }
}
