package com.app.greenskeeper.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.app.greenskeeper.domain.Category;
import com.app.greenskeeper.domain.LightRequirement;
import com.app.greenskeeper.domain.WateringDuration;
import com.app.greenskeeper.entity.CategoryDetails;
import com.app.greenskeeper.exception.CategoryAlreadyExistsException;
import com.app.greenskeeper.repository.CategoryRepository;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CategoryServiceTest {

  @Mock
  private CategoryRepository categoryRepository;
  @InjectMocks
  private CategoryService categoryService;

  @Test
  public void testGetAllCategory() {
    CategoryDetails foliageCategory = CategoryDetails.builder()
                                                     .id(UUID.randomUUID())
                                                     .name("Foliage Plant")
                                                     .duration(WateringDuration.WEEKLY)
                                                     .wateringPeriod(2)
                                                     .lightRequirement(LightRequirement.INDIRECTLIGHT)
                                                     .build();

    CategoryDetails cactiCategory = CategoryDetails.builder()
                                                   .id(UUID.randomUUID())
                                                   .name("Cacti Plant")
                                                   .duration(WateringDuration.MONTHLY)
                                                   .wateringPeriod(2)
                                                   .lightRequirement(LightRequirement.LOWLIGHT)
                                                   .build();

    List<CategoryDetails> categoryEntities = Arrays.asList(foliageCategory, cactiCategory);

    given(categoryRepository.findAll()).willReturn(categoryEntities);

    List<Category> categories = categoryService.getAllCategory();

    assertThat(categories).isNotEmpty();
    assertThat(categories).size().isEqualTo(2);
    assertThat(categories.get(0).getId()).isEqualTo(foliageCategory.getId());
    assertThat(categories.get(0).getTitle()).isEqualTo(foliageCategory.getName());
    assertThat(categories.get(0).getDuration())
        .isEqualTo(foliageCategory.getDuration());
    assertThat(categories.get(0).getWateringPeriod())
        .isEqualTo(String.valueOf(foliageCategory.getWateringPeriod()));
    assertThat(categories.get(0).getLightRequirement())
        .isEqualTo(LightRequirement.INDIRECTLIGHT);

    assertThat(categories.get(1).getId()).isEqualTo(cactiCategory.getId());
    assertThat(categories.get(1).getTitle()).isEqualTo(cactiCategory.getName());
    assertThat(categories.get(1).getDuration())
        .isEqualTo(cactiCategory.getDuration());
    assertThat(categories.get(1).getWateringPeriod())
        .isEqualTo(String.valueOf(cactiCategory.getWateringPeriod()));
    assertThat(categories.get(1).getLightRequirement())
        .isEqualTo(LightRequirement.LOWLIGHT);

  }

  @Test
  public void testGetAllCategory_whenCategoryIsEmpty() {
    given(categoryRepository.findAll()).willReturn(Collections.emptyList());

    List<Category> categories = categoryService.getAllCategory();

    assertThat(categories).isEmpty();
    assertThat(categories).size().isEqualTo(0);
  }

  @Test
  public void testCreateCategory() {
    Category category = Category.builder()
                                .title("Cacti Plant")
                                .duration(WateringDuration.MONTHLY)
                                .wateringPeriod("2")
                                .lightRequirement(LightRequirement.LOWLIGHT)
                                .build();
    category = categoryService.createCategory(category);
    verify(categoryRepository, times(1)).save(buildCategoryEntity(category));
  }

  @Test
  public void testCreateCategory_whenCategoryNameAlreadyExists() {
    Category category = Category.builder()
                                .title("Cacti Plant")
                                .duration(WateringDuration.MONTHLY)
                                .wateringPeriod("2")
                                .lightRequirement(LightRequirement.LOWLIGHT)
                                .build();
    given(categoryRepository.findByName("Cacti Plant"))
        .willReturn(Optional.of(buildCategoryEntity(category)));

    assertThatThrownBy(() -> categoryService.createCategory(buildCategory()))
        .isInstanceOf(CategoryAlreadyExistsException.class)
        .hasMessageContaining("Category already exists.Please try with another one");

  }

  private Category buildCategory() {
    return Category.builder()
                   .title("Cacti Plant")
                   .duration(WateringDuration.MONTHLY)
                   .wateringPeriod(String.valueOf("2"))
                   .lightRequirement(LightRequirement.LOWLIGHT)
                   .build();
  }

  private CategoryDetails buildCategoryEntity(Category category) {
    return CategoryDetails.builder()
                          .name(category.getTitle())
                          .duration(category.getDuration())
                          .wateringPeriod(Integer.valueOf(category.getWateringPeriod()))
                          .lightRequirement(category.getLightRequirement())
                          .build();
  }

}
