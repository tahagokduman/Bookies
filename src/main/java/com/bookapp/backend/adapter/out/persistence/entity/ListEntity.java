package com.bookapp.backend.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "lists")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class ListEntity extends BaseEntity<Long> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "list_name", nullable = false)
    private String listName;
}