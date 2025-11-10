package com.bookapp.backend.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "status")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class StatusEntity extends BaseEntity<Long> {

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "sort_order")
    private Integer sortOrder;

}