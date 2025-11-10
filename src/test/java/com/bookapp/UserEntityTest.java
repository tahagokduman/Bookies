package com.bookapp;

import com.bookapp.backend.adapter.out.persistence.entity.UserEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class UserEntityTest {

    @Test
    void testBuilderAndGetters() {
        LocalDate birthDate = LocalDate.of(2000, 1, 1);
        UserEntity user = UserEntity.builder()
                .id(1L)
                .username("testuser")
                .email("test@mail.com")
                .password("securePass123")
                .birthdayDate(birthDate)
                .followersCount(100)
                .followedPersonCount(50)
                .build();

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getUsername()).isEqualTo("testuser");
        assertThat(user.getEmail()).isEqualTo("test@mail.com");
        assertThat(user.getPassword()).isEqualTo("securePass123");
        assertThat(user.getBirthdayDate()).isEqualTo(birthDate);
        assertThat(user.getFollowersCount()).isEqualTo(100);
        assertThat(user.getFollowedPersonCount()).isEqualTo(50);
    }

    @Test
    void testSettersAndGetters() {
        UserEntity user = new UserEntity();
        user.setId(2L);
        user.setUsername("setterUser");
        user.setEmail("setter@mail.com");
        user.setPassword("pass123");
        user.setBirthdayDate(LocalDate.of(1999, 5, 5));
        user.setFollowersCount(10);
        user.setFollowedPersonCount(5);

        assertThat(user.getId()).isEqualTo(2L);
        assertThat(user.getUsername()).isEqualTo("setterUser");
        assertThat(user.getEmail()).isEqualTo("setter@mail.com");
        assertThat(user.getPassword()).isEqualTo("pass123");
        assertThat(user.getBirthdayDate()).isEqualTo(LocalDate.of(1999, 5, 5));
        assertThat(user.getFollowersCount()).isEqualTo(10);
        assertThat(user.getFollowedPersonCount()).isEqualTo(5);
    }


}