package com.app.greenskeeper.repository;

import com.app.greenskeeper.entity.PlantDetails;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlantRepository extends CrudRepository<PlantDetails, UUID> {

  Optional<PlantDetails> findByName(String name);

  @Override
  List<PlantDetails> findAll();

}
