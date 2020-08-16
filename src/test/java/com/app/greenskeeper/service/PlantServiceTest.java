package com.app.greenskeeper.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.app.greenskeeper.domain.Category;
import com.app.greenskeeper.domain.LightRequirement;
import com.app.greenskeeper.domain.Plant;
import com.app.greenskeeper.domain.WateringDuration;
import com.app.greenskeeper.entity.CategoryEntity;
import com.app.greenskeeper.entity.PlantEntity;
import com.app.greenskeeper.exception.PlantAlreadyExistsException;
import com.app.greenskeeper.exception.PlantNotFoundException;
import com.app.greenskeeper.repository.PlantRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PlantServiceTest {

  @Mock
  private PlantRepository plantRepository;
  @InjectMocks
  private PlantService plantService;

  @Test
  public void testAddPlant() {
    Plant plant = Plant.builder()
                       .name("Peace Lilly")
                       .category(buildPlantCategory())
                       .build();
    plantService.addPlant(plant);
    verify(plantRepository, times(1)).save(buildPlantEntity());
  }

  @Test
  public void testAddPlant_whenPlatNameAlreadyExists() {
    given(plantRepository.findByName("Peace Lilly")).willReturn(Optional.of(buildPlantEntity()));
    assertThatThrownBy(() -> plantService.addPlant(buildPlant()))
        .isInstanceOf(PlantAlreadyExistsException.class)
        .hasMessageContaining("Similar plant already exists.Please choose a new name");
  }

  @Test
  public void testGetPlant() {
    UUID plantId = UUID.fromString("aaa46f55-3a2a-4d33-80e9-9b3dc1a88ad7");
    given(plantRepository.findById(plantId)).willReturn(Optional.of(buildPlantEntity()));
    Plant plant = plantService.getPlant(plantId);

    assertThat(plant.getId()).isNotNull();
    assertThat(plant.getName()).isEqualTo("Peace Lilly");
    assertThat(plant.getCategory().getId()).isEqualTo(
        UUID.fromString("caa46f55-3a2a-4d33-80e9-9b3dc1a88ad9"));
    assertThat(plant.getCategory().getTitle()).isEqualTo("Foliage Plant");
    assertThat(plant.getCategory().getDuration()).isEqualTo(WateringDuration.WEEKLY);
    assertThat(plant.getCategory().getFrequency()).isEqualTo("2");
    assertThat(plant.getCategory().getLightRequirement()).isEqualTo(LightRequirement.INDIRECTLIGHT);
  }

  @Test
  public void testGetPlant_whenPlantNotFound() {
    UUID plantId = UUID.fromString("aaa46f55-3a2a-4d33-80e9-9b3dc1a88ad7");
    given(plantRepository.findById(plantId))
        .willThrow(new PlantNotFoundException("Cannot find the plant that you are looking for"));
    assertThatThrownBy(() -> plantService.getPlant(plantId))
        .isInstanceOf(PlantNotFoundException.class)
        .hasMessageContaining("Cannot find the plant that you are looking for");
  }

  @Test
  public void testGetAllPlants() {
    CategoryEntity trailingCategory = CategoryEntity.builder()
                                                    .id(UUID.randomUUID())
                                                    .name("Trailing Plant")
                                                    .duration(WateringDuration.WEEKLY)
                                                    .frequency(2)
                                                    .lightRequirement(
                                                        LightRequirement.INDIRECTLIGHT)
                                                    .build();

    PlantEntity pothos = PlantEntity.builder()
                                    .name("Pothos")
                                    .category(trailingCategory)
                                    .build();

    CategoryEntity plamCategory = CategoryEntity.builder()
                                                .id(UUID.randomUUID())
                                                .name("Palm Plant")
                                                .duration(WateringDuration.MONTHLY)
                                                .frequency(4)
                                                .lightRequirement(LightRequirement.INDIRECTLIGHT)
                                                .build();

    PlantEntity arecaPalm = PlantEntity.builder()
                                       .name("Areca Palm")
                                       .category(plamCategory)
                                       .build();

    List<PlantEntity> plantEntities = Arrays.asList(pothos, arecaPalm);

    given(plantRepository.findAll()).willReturn(plantEntities);

    List<Plant> availablePlants = plantService.getAllPlants();
    assertThat(availablePlants).isNotEmpty();
    assertThat(availablePlants.get(0).getName()).isEqualTo("Pothos");
    assertThat(availablePlants.get(0).getCategory().getTitle()).isEqualTo("Trailing Plant");
    assertThat(availablePlants.get(0).getCategory().getDuration())
        .isEqualTo(WateringDuration.WEEKLY);
    assertThat(availablePlants.get(0).getCategory().getFrequency()).isEqualTo("2");
    assertThat(availablePlants.get(0).getCategory().getLightRequirement())
        .isEqualTo(LightRequirement.INDIRECTLIGHT);

    assertThat(availablePlants.get(1).getName()).isEqualTo("Areca Palm");
    assertThat(availablePlants.get(1).getCategory().getTitle()).isEqualTo("Palm Plant");
    assertThat(availablePlants.get(1).getCategory().getDuration())
        .isEqualTo(WateringDuration.MONTHLY);
    assertThat(availablePlants.get(1).getCategory().getFrequency()).isEqualTo("4");
    assertThat(availablePlants.get(1).getCategory().getLightRequirement())
        .isEqualTo(LightRequirement.INDIRECTLIGHT);
  }

  private Plant buildPlant() {
    return Plant.builder()
                .name("Peace Lilly")
                .category(buildPlantCategory())
                .build();
  }

  private Category buildPlantCategory() {
    return Category.builder()
                   .id(UUID.fromString("baa46f55-3a2a-4d33-80e9-9b3dc1a88ad8"))
                   .title("Foliage Plant")
                   .duration(WateringDuration.WEEKLY)
                   .frequency("2")
                   .lightRequirement(LightRequirement.INDIRECTLIGHT)
                   .build();
  }

  private PlantEntity buildPlantEntity() {
    return PlantEntity.builder()
                      .id(UUID.fromString("aaa46f55-3a2a-4d33-80e9-9b3dc1a88ad7"))
                      .name("Peace Lilly")
                      .category(buildCategoryEntity())
                      .build();
  }

  private CategoryEntity buildCategoryEntity() {
    return CategoryEntity.builder()
                         .id(UUID.fromString("caa46f55-3a2a-4d33-80e9-9b3dc1a88ad9"))
                         .name("Foliage Plant")
                         .duration(WateringDuration.WEEKLY)
                         .frequency(2)
                         .lightRequirement(LightRequirement.INDIRECTLIGHT).build();
  }
}
