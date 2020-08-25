package com.app.greenskeeper.entity;

import io.leangen.graphql.annotations.types.GraphQLType;
import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

@Entity(name = "PlantDetails")
@Table(name = "plant_details")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@GraphQLType
public class PlantDetails extends BaseEntity {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;
  @Column(name = "plant_name")
  @NonNull
  private String name;
  @Column(name = "category")
  private String category;
  @Column(name = "watering_interval")
  private Integer wateringInterval;
  @OneToOne(mappedBy = "plantDetails", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private WateringDetails wateringDetails;
  @OneToMany(mappedBy = "wateringHistoryPlantDetails", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<WateringHistoryDetails> wateringHistoryDetails;
}
