package com.app.greenskeeper.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.app.greenskeeper.entity.CategoryDetails;

@Repository
public interface CategoryRepository extends CrudRepository<CategoryDetails, UUID> {

  @Override
  List<CategoryDetails> findAll();

  Optional<CategoryDetails> findByName(String name);
}
