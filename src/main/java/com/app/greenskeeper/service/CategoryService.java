package com.app.greenskeeper.service;

import com.app.greenskeeper.domain.Category;
import com.app.greenskeeper.entity.CategoryEntity;
import com.app.greenskeeper.exception.CategoryAlreadyExistsException;
import com.app.greenskeeper.repository.CategoryRepository;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@GraphQLApi
@Service
@RequiredArgsConstructor
public class CategoryService {

  @NonNull
  private CategoryRepository categoryRepository;

  @GraphQLQuery
  public List<Category> getAllCategory() {
    List<CategoryEntity> categoryEntities = categoryRepository.findAll();
    return buildCatergoriesResponse(categoryEntities);
  }

  @NonNull
  @GraphQLMutation(name = "createCategory")
  public Category createCategory(@GraphQLArgument(name = "category") Category category) {
    if (categoryRepository.findByName(category.getTitle()).isPresent()) {
      throw new CategoryAlreadyExistsException(
          "Category already exists.Please try with another one");
    }
    CategoryEntity categoryEntity = buildCategoryEntity(category);
    categoryRepository.save(categoryEntity);
    return buildCategoryResponse(categoryEntity);
  }

  private List<Category> buildCatergoriesResponse(List<CategoryEntity> categoryEntities) {
    return categoryEntities.stream().map(this::buildCategoryResponse).collect(Collectors.toList());
  }

  private Category buildCategoryResponse(CategoryEntity categoryEntity) {
    return Category.builder()
                   .id(categoryEntity.getId())
                   .title(categoryEntity.getName())
                   .duration(categoryEntity.getDuration())
                   .frequency(String.valueOf(categoryEntity.getFrequency()))
                   .lightRequirement(categoryEntity.getLightRequirement())
                   .build();
  }


  private CategoryEntity buildCategoryEntity(Category category) {
    return CategoryEntity.builder()
                         .name(category.getTitle())
                         .duration(category.getDuration())
                         .frequency(Integer.valueOf(category.getFrequency()))
                         .lightRequirement(category.getLightRequirement())
                         .build();
  }

}
