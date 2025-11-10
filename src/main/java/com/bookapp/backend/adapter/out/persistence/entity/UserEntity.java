package com.bookapp.backend.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "users")
public class UserEntity extends BaseEntity<Long> {

    @Column( unique = true)
    private String username;

    @Column( unique = true)
    private String email;

    @Column()
    private String password;

    @Column(name = "birthday_date")
    private LocalDate birthdayDate;

    @Column(name = "followers_count")
    private Integer followersCount;

    @Column(name = "followed_person_count")
    private Integer followedPersonCount;
}
