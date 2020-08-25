package com.app.greenskeeper.entity;

import io.leangen.graphql.annotations.types.GraphQLType;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

@Entity(name = "WateringHistoryDetails")
@Table(name = "watering_history_details")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@GraphQLType
public class WateringHistoryDetails extends BaseEntity {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  private UUID id;
  @Basic
  @Column(name = "watering_time")
  private LocalDateTime wateringTime;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "plant_id", referencedColumnName = "id")
  private PlantDetails wateringHistoryPlantDetails;
}
