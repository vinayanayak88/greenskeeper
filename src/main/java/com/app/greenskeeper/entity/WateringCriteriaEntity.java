//package com.app.greenskeeper.entity;
//
//import io.leangen.graphql.annotations.types.GraphQLType;
//import java.util.UUID;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.EnumType;
//import javax.persistence.Enumerated;
//import javax.persistence.GeneratedValue;
//import javax.persistence.Id;
//import javax.persistence.OneToOne;
//import javax.persistence.Table;
//
//import org.hibernate.annotations.GenericGenerator;
//
//import com.app.greenskeeper.domain.WateringDuration;
//
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.EqualsAndHashCode;
//import lombok.NoArgsConstructor;
//import lombok.NonNull;
//import lombok.experimental.Accessors;
//
//@Entity
//@Table(name = "watering_criteria")
//@Data
//@EqualsAndHashCode(callSuper = true)
//@Accessors(chain = true)
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//@GraphQLType
//public class WateringCriteriaEntity extends BaseEntity {
//
//    @Id
//    @GeneratedValue(generator = "UUID")
//    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
//    @Column(name = "id", updatable = false, nullable = false)
//    private UUID id;
//    @Column(name = "duration")
//    @Enumerated(EnumType.STRING)
//    @NonNull
//    private WateringDuration duration;
//    @NonNull
//    @Column(name = "frequency")
//    private Integer frequency;
//    @OneToOne(mappedBy = "wateringCriteriaEntity")
//    private CategoryEntity categoryEntity;
//
//}
