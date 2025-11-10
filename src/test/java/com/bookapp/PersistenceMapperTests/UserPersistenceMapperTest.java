package com.bookapp.PersistenceMapperTests;

import com.bookapp.backend.adapter.out.persistence.entity.UserEntity;
import com.bookapp.backend.adapter.out.persistence.mapper.UserPersistenceMapper;
import com.bookapp.backend.domain.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserPersistenceMapperTest {

    private UserPersistenceMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new UserPersistenceMapper();
    }

    @Test
    void toDomain_shouldMapEntityToDomain() {
        UserEntity entity = new UserEntity();
        entity.setId(1L);
        entity.setUsername("testuser");
        entity.setEmail("test@example.com");
        entity.setPassword("secret");
        entity.setBirthdayDate(LocalDate.of(2000, 1, 1));
        entity.setFollowersCount(5);
        entity.setFollowedPersonCount(3);

        User user = mapper.toDomain(entity);

        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("testuser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("secret", user.getPassword());
        assertEquals(LocalDate.of(2000, 1, 1), user.getBirthdayDate());
        assertEquals(5, user.getFollowersCount().getValue());
        assertEquals(3, user.getFollowingCount().getValue());
    }

    @Test
    void toEntity_shouldMapDomainToEntity() {
        User user = new User(
                1L,
                "testuser",
                "test@example.com",
                "secret",
                LocalDate.of(2000, 1, 1),
                new com.bookapp.backend.domain.model.base.NonNegativeInteger(5),
                new com.bookapp.backend.domain.model.base.NonNegativeInteger(3)
        );

        UserEntity entity = mapper.toEntity(user);

        assertNotNull(entity);
        assertEquals(1L, entity.getId());
        assertEquals("testuser", entity.getUsername());
        assertEquals("test@example.com", entity.getEmail());
        assertEquals("secret", entity.getPassword());
        assertEquals(LocalDate.of(2000, 1, 1), entity.getBirthdayDate());
        assertEquals(5, entity.getFollowersCount());
        assertEquals(3, entity.getFollowedPersonCount());
    }

    @Test
    void toDomain_shouldReturnNull_whenEntityIsNull() {
        assertNull(mapper.toDomain(null));
    }

    @Test
    void toEntity_shouldReturnNull_whenDomainIsNull() {
        assertNull(mapper.toEntity(null));
    }
}
