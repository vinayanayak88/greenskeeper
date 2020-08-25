package com.app.greenskeeper.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.app.greenskeeper.domain.Plant;
import com.app.greenskeeper.entity.PlantDetails;
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
                       .category("Trailing Plant")
                       .wateringInterval("3")
                       .build();
    given(plantRepository.findByName(plant.getName())).willReturn(Optional.empty());
    plantService.addPlant(plant);
    PlantDetails plantDetails = PlantDetails.builder()
                                            .name("Peace Lilly")
                                            .category("Trailing Plant")
                                            .wateringInterval(3)
                                            .build();
    verify(plantRepository, times(1)).save(plantDetails);
  }

  @Test
  public void testAddPlant_whenPlatNameAlreadyExists() {
    given(plantRepository.findByName("Peace Lilly")).willReturn(Optional.of(buildPlantDetails()));
    assertThatThrownBy(() -> plantService.addPlant(buildPlant()))
        .isInstanceOf(PlantAlreadyExistsException.class)
        .hasMessageContaining(
            "Plant with the similar name already exists.Please choose another name");
  }

  @Test
  public void testGetPlant() {
    UUID plantId = UUID.fromString("aaa46f55-3a2a-4d33-80e9-9b3dc1a88ad7");
    given(plantRepository.findById(plantId)).willReturn(Optional.of(buildPlantDetails()));
    Plant plant = plantService.getPlant(plantId);

    assertThat(plant.getId()).isNotNull();
    assertThat(plant.getName()).isEqualTo("Peace Lilly");
    assertThat(plant.getCategory()).isEqualTo("Foliage Plant");
    assertThat(plant.getWateringInterval()).isEqualTo("3");
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

    PlantDetails pothos = PlantDetails.builder()
                                      .name("Pothos")
                                      .category("Trailing Plant")
                                      .wateringInterval(2)
                                      .build();

    PlantDetails arecaPalm = PlantDetails.builder()
                                         .name("Areca Palm")
                                         .category("Palm Plant")
                                         .wateringInterval(5)
                                         .build();

    List<PlantDetails> plantEntities = Arrays.asList(pothos, arecaPalm);

    given(plantRepository.findAll()).willReturn(plantEntities);

    List<Plant> availablePlants = plantService.getAllPlants();
    assertThat(availablePlants).isNotEmpty();
    assertThat(availablePlants.get(0).getName()).isEqualTo("Pothos");
    assertThat(availablePlants.get(0).getCategory()).isEqualTo("Trailing Plant");
    assertThat(availablePlants.get(0).getWateringInterval()).isEqualTo("2");

    assertThat(availablePlants.get(1).getName()).isEqualTo("Areca Palm");
    assertThat(availablePlants.get(1).getCategory()).isEqualTo("Palm Plant");
    assertThat(availablePlants.get(1).getWateringInterval()).isEqualTo("5");
  }

  private Plant buildPlant() {
    return Plant.builder()
                .name("Peace Lilly")
                .category("Foliage Plant")
                .wateringInterval("3")
                .build();
  }

  private PlantDetails buildPlantDetails() {
    return PlantDetails.builder()
                       .id(UUID.fromString("aaa46f55-3a2a-4d33-80e9-9b3dc1a88ad7"))
                       .name("Peace Lilly")
                       .category("Foliage Plant")
                       .wateringInterval(3)
                       .build();
  }

}
