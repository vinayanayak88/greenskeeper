package com.app.greenskeeper.entity;

import com.app.greenskeeper.domain.LightRequirement;
import com.app.greenskeeper.domain.WateringDuration;
import io.leangen.graphql.annotations.types.GraphQLType;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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

@Entity
@Table(name = "category")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@GraphQLType
public class CategoryEntity extends BaseEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    @Column(name = "title")
    private String name;
    @Column(name = "duration")
    @Enumerated(EnumType.STRING)
    @NonNull
    private WateringDuration duration;
    @Column(name = "watering_period")
    private Integer wateringPeriod;
    @Column(name = "light_requirement")
    @Enumerated(EnumType.STRING)
    @NonNull
    private LightRequirement lightRequirement;
    @OneToOne(mappedBy = "category")
    private PlantEntity plant;

}
