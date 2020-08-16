package com.app.greenskeeper.repository;

import com.app.greenskeeper.entity.PlantEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlantRepository extends CrudRepository<PlantEntity, UUID> {

  Optional<PlantEntity> findByName(String name);

  @Override
  List<PlantEntity> findAll();

}
