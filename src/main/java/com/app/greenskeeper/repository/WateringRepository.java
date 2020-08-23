package com.app.greenskeeper.repository;

import com.app.greenskeeper.entity.WateringDetails;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WateringRepository extends CrudRepository<WateringDetails, UUID> {

}
