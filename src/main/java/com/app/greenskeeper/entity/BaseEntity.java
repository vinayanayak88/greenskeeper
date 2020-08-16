package com.app.greenskeeper.entity;

import io.leangen.graphql.annotations.types.GraphQLType;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;
import lombok.experimental.Accessors;

@MappedSuperclass
@Data
@Accessors(chain = true)
@GraphQLType
public class BaseEntity {

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
