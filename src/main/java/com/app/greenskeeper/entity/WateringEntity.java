package com.app.greenskeeper.entity;

import com.app.greenskeeper.domain.WateringHistory;
import io.leangen.graphql.annotations.types.GraphQLType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "watering")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@GraphQLType
public class WateringEntity extends BaseEntity {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  private UUID id;
  @Column(name = "plant_id")
  private UUID plantId;
  @Basic
  private LocalDateTime lastWateredOn;
  @Basic
  private LocalDateTime nextWateringDay;
  @OneToMany
  @JoinColumn(name = "cart_id")
  private List<WateringHistoryEntity> wateringHistory;

}
