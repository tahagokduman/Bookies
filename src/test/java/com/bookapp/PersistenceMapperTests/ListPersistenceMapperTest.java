package com.bookapp.PersistenceMapperTests;

import com.bookapp.backend.adapter.out.persistence.entity.ListEntity;
import com.bookapp.backend.adapter.out.persistence.entity.UserEntity;
import com.bookapp.backend.adapter.out.persistence.mapper.ListPersistenceMapper;
import com.bookapp.backend.adapter.out.persistence.mapper.UserPersistenceMapper;
import com.bookapp.backend.domain.model.list.List;
import com.bookapp.backend.domain.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ListPersistenceMapperTest {

    private UserPersistenceMapper userMapper;
    private ListPersistenceMapper mapper;

    @BeforeEach
    void setUp() {
        userMapper = mock(UserPersistenceMapper.class);
        mapper = new ListPersistenceMapper(userMapper);
    }

    @Test
    void toDomain_shouldMapEntityToDomain() {
        ListEntity entity = new ListEntity();
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        entity.setId(10L);
        entity.setUser(userEntity);
        entity.setListName("Meine Bücher");

        User user = new User(1L);
        when(userMapper.toDomain(userEntity)).thenReturn(user);

        List domain = mapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(10L, domain.getId());
        assertEquals(user, domain.getUser());
        assertEquals("Meine Bücher", domain.getName());
    }

    @Test
    void toEntity_shouldMapDomainToEntity() {
        User user = new User(1L);
        List domain = new List(10L, user, "Leseliste");

        UserEntity userEntity = new UserEntity();
        when(userMapper.toEntity(user)).thenReturn(userEntity);

        ListEntity entity = mapper.toEntity(domain);

        assertNotNull(entity);
        assertEquals(10L, entity.getId());
        assertEquals(userEntity, entity.getUser());
        assertEquals("Leseliste", entity.getListName());
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
