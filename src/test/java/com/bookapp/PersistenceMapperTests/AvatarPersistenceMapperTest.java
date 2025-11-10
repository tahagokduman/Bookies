package com.bookapp.PersistenceMapperTests;

import com.bookapp.backend.adapter.out.persistence.entity.AvatarEntity;
import com.bookapp.backend.adapter.out.persistence.mapper.AvatarPersistenceMapper;
import com.bookapp.backend.domain.model.user.Avatar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AvatarPersistenceMapperTest {

    private AvatarPersistenceMapper avatarPersistenceMapper;

    @BeforeEach
    void setUp() {
        avatarPersistenceMapper = new AvatarPersistenceMapper();
    }

    @Test
    void toEntity_shouldMapCorrectly() {
        Avatar avatarDomain = new Avatar();
        avatarDomain.setId(1L);
        avatarDomain.setAvatar("testAvatar");

        AvatarEntity avatarEntity = avatarPersistenceMapper.toEntity(avatarDomain);

        assertNotNull(avatarEntity);
        assertEquals(1L, avatarEntity.getId());
        assertEquals("testAvatar", avatarEntity.getAvatar());
    }

    @Test
    void toDomain_shouldMapCorrectly() {
        AvatarEntity avatarEntity = new AvatarEntity();
        avatarEntity.setId(2L);
        avatarEntity.setAvatar("testAvatar");

        Avatar avatarDomain = avatarPersistenceMapper.toDomain(avatarEntity);

        assertNotNull(avatarDomain);
        assertEquals(2L, avatarDomain.getId());
        assertEquals("testAvatar", avatarDomain.getAvatar());
    }

    @Test
    void toEntity_shouldThrowNullPointer_whenInputIsNull() {
        assertThrows(NullPointerException.class, () -> {
            avatarPersistenceMapper.toEntity(null);
        });
    }

    @Test
    void toDomain_shouldThrowNullPointer_whenInputIsNull() {
        assertThrows(NullPointerException.class, () -> {
            avatarPersistenceMapper.toDomain(null);
        });
    }
}
