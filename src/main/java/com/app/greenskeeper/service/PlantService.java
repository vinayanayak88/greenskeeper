package com.app.greenskeeper.service;

import com.app.greenskeeper.domain.Plant;
import com.app.greenskeeper.domain.WateringHistory;
import com.app.greenskeeper.domain.WateringInformation;
import com.app.greenskeeper.entity.PlantDetails;
import com.app.greenskeeper.exception.PlantAlreadyExistsException;
import com.app.greenskeeper.exception.PlantNotFoundException;
import com.app.greenskeeper.repository.PlantRepository;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@GraphQLApi
@Service
@RequiredArgsConstructor
public class PlantService {

  @NonNull
  private PlantRepository plantRepository;

  @NonNull
  @GraphQLMutation(name = "addPlant")
  public Plant addPlant(@GraphQLArgument(name = "plant") Plant plant) {
    if (plantRepository.findByName(plant.getName()).isPresent()) {
      throw new PlantAlreadyExistsException(
          "Plant with the similar name already exists.Please choose another name");
    }
    PlantDetails plantDetails = buildPlantDetails(plant);
    plantRepository.save(plantDetails);
    return buildPlantResponse(plantDetails);
  }

  @NonNull
  @GraphQLQuery(name = "getPlant")
  public Plant getPlant(@GraphQLArgument(name = "id") UUID id) {
    PlantDetails plantDetails = plantRepository.findById(id).orElseThrow(() ->
                                                                             new PlantNotFoundException(
                                                                                 "Cannot find the plant that you are looking for"));
    return buildPlantResponse(plantDetails);
  }

  @NonNull
  @GraphQLQuery(name = "getAllPlants")
  public List<Plant> getAllPlants() {
    List<PlantDetails> allPlants = plantRepository.findAll();
    allPlants.sort((p1, p2) -> p1.getWateringDetails().getNextWateringDay().compareTo(p2.getWateringDetails().getNextWateringDay()));
    return allPlants.stream().map(this::buildPlantResponse).collect(Collectors.toList());
  }

  @NonNull
  @GraphQLQuery(name = "removePlant")
  public void removePlant(@GraphQLArgument(name = "id") UUID id){
    plantRepository.deleteById(id);
  }


  private PlantDetails buildPlantDetails(Plant plant) {
    return PlantDetails.builder()
                       .name(plant.getName())
                       .category(plant.getCategory())
                       .wateringInterval(Integer.parseInt(plant.getWateringInterval()))
                       .build();
  }

  private Plant buildPlantResponse(PlantDetails plantDetails) {
    return Plant.builder()
                .id(plantDetails.getId())
                .name(plantDetails.getName())
                .category(plantDetails.getCategory())
                .wateringInterval(String.valueOf(plantDetails.getWateringInterval()))
                .wateringInformation(plantDetails.getWateringDetails() != null ? buildWateringInformation(plantDetails) : null)
                .wateringHistories(buildWateringHistories(plantDetails))
                .build();
  }

  private WateringInformation buildWateringInformation(PlantDetails plantDetails) {
    return WateringInformation.builder()
                              .id(plantDetails.getWateringDetails().getId())
                              .lastWateredOn(plantDetails.getWateringDetails().getLastWateredOn())
                              .nextWateringDay(
                                  plantDetails.getWateringDetails().getNextWateringDay())
                              .build();
  }

  private List<WateringHistory> buildWateringHistories(PlantDetails plantDetails) {
    return plantDetails.getWateringHistoryDetails().stream()
                       .map(w -> new WateringHistory(w.getId(), w.getWateringTime()))
                       .collect(Collectors.toList());
  }
}
