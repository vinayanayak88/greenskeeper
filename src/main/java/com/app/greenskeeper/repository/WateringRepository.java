package com.app.greenskeeper.repository;

import com.app.greenskeeper.entity.PlantEntity;
import com.app.greenskeeper.entity.WateringEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WateringRepository extends CrudRepository<WateringEntity, UUID> {

  Optional<WateringEntity> findByPlantId(UUID plantId);

}
