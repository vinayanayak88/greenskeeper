package com.app.greenskeeper.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.app.greenskeeper.entity.CategoryEntity;

@Repository
public interface CategoryRepository extends CrudRepository<CategoryEntity, UUID> {

  @Override
  List<CategoryEntity> findAll();

  Optional<CategoryEntity> findByName(String name);
}
