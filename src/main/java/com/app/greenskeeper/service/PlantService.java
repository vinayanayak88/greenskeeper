package com.app.greenskeeper.service;

import com.app.greenskeeper.domain.Category;
import com.app.greenskeeper.domain.Plant;
import com.app.greenskeeper.entity.CategoryEntity;
import com.app.greenskeeper.entity.PlantEntity;
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
    PlantEntity plantEntity = buildPlantEntity(plant);
    plantRepository.save(plantEntity);
    return buildPlantResponse(plantEntity);
  }

  @NonNull
  @GraphQLQuery(name = "getPlant")
  public Plant getPlant(@GraphQLArgument(name = "id") UUID id) {
    PlantEntity plantEntity = plantRepository.findById(id).orElseThrow(() ->
        new PlantNotFoundException("Cannot find the plant that you are looking for"));
    return buildPlantResponse(plantEntity);
  }

  @NonNull
  @GraphQLQuery(name = "getAllPlants")
  public List<Plant> getAllPlants(){
    List<PlantEntity> allPlants =  plantRepository.findAll();
    return  allPlants.stream().map(this::buildPlantResponse).collect(Collectors.toList());
  }

  private PlantEntity buildPlantEntity(Plant plant) {
    return PlantEntity.builder()
                      .name(plant.getName())
                      .category(buildCategoryEntity(plant.getCategory()))
                      .build();
  }

  private CategoryEntity buildCategoryEntity(Category category) {
    return CategoryEntity.builder()
                         .name(category.getTitle())
                         .duration(category.getDuration())
                         .wateringPeriod(Integer.valueOf(category.getWateringPeriod()))
                         .lightRequirement(category.getLightRequirement())
                         .build();
  }

  private Plant buildPlantResponse(PlantEntity plantEntity) {
    return Plant.builder()
                .id(plantEntity.getId())
                .name(plantEntity.getName())
                .category(buildCategoryResponse(plantEntity.getCategory()))
                .build();
  }

  private Category buildCategoryResponse(CategoryEntity categoryEntity) {
    return Category.builder()
                   .id(categoryEntity.getId())
                   .title(categoryEntity.getName())
                   .duration(categoryEntity.getDuration())
                   .wateringPeriod(String.valueOf(categoryEntity.getWateringPeriod()))
                   .lightRequirement(categoryEntity.getLightRequirement())
                   .build();
  }

}
