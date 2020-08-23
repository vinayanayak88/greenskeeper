package com.app.greenskeeper.service;

import com.app.greenskeeper.domain.Category;
import com.app.greenskeeper.domain.Plant;
import com.app.greenskeeper.entity.CategoryDetails;
import com.app.greenskeeper.entity.PlantDetails;
import com.app.greenskeeper.exception.CategoryNotFoundException;
import com.app.greenskeeper.exception.PlantAlreadyExistsException;
import com.app.greenskeeper.exception.PlantNotFoundException;
import com.app.greenskeeper.repository.CategoryRepository;
import com.app.greenskeeper.repository.PlantRepository;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import java.util.List;
import java.util.Optional;
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
  private CategoryRepository categoryRepository;

  @NonNull
  @GraphQLMutation(name = "addPlant")
  public Plant addPlant(@GraphQLArgument(name = "plant") Plant plant) {
    if (plantRepository.findByName(plant.getName()).isPresent()) {
      throw new PlantAlreadyExistsException(
          "Plant with the similar name already exists.Please choose another name");
    }
    Optional<CategoryDetails> categoryDetails = categoryRepository.findByName(plant.getCategory().getTitle());
    if(categoryDetails.isEmpty()){
      throw new CategoryNotFoundException("Could not find the plant category");
    }
    PlantDetails plantDetails = buildPlantDetails(plant);
    plantRepository.save(plantDetails);
    return buildPlantResponse(plantDetails);
  }

  @NonNull
  @GraphQLQuery(name = "getPlant")
  public Plant getPlant(@GraphQLArgument(name = "id") UUID id) {
    PlantDetails plantDetails = plantRepository.findById(id).orElseThrow(() ->
        new PlantNotFoundException("Cannot find the plant that you are looking for"));
    return buildPlantResponse(plantDetails);
  }

  @NonNull
  @GraphQLQuery(name = "getAllPlants")
  public List<Plant> getAllPlants(){
    List<PlantDetails> allPlants =  plantRepository.findAll();
    return  allPlants.stream().map(this::buildPlantResponse).collect(Collectors.toList());
  }

  private PlantDetails buildPlantDetails(Plant plant) {
    return PlantDetails.builder()
                       .name(plant.getName())
                       .categoryDetails(buildCategoryDetails(plant.getCategory()))
                       .build();
  }

  private CategoryDetails buildCategoryDetails(Category category) {
    return CategoryDetails.builder()
                          .name(category.getTitle())
                          .duration(category.getDuration())
                          .wateringPeriod(Integer.valueOf(category.getWateringPeriod()))
                          .lightRequirement(category.getLightRequirement())
                          .build();
  }

  private Plant buildPlantResponse(PlantDetails plantDetails) {
    return Plant.builder()
                .id(plantDetails.getId())
                .name(plantDetails.getName())
                .category(buildCategoryResponse(plantDetails.getCategoryDetails()))
                .build();
  }

  private Category buildCategoryResponse(CategoryDetails categoryEntity) {
    return Category.builder()
                   .id(categoryEntity.getId())
                   .title(categoryEntity.getName())
                   .duration(categoryEntity.getDuration())
                   .wateringPeriod(String.valueOf(categoryEntity.getWateringPeriod()))
                   .lightRequirement(categoryEntity.getLightRequirement())
                   .build();
  }

}
