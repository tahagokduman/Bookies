package com.bookapp.PersistenceMapperTests;

import com.bookapp.backend.adapter.out.persistence.entity.FollowerEntity;
import com.bookapp.backend.adapter.out.persistence.entity.UserEntity;
import com.bookapp.backend.adapter.out.persistence.mapper.FollowerPersistenceMapper;
import com.bookapp.backend.adapter.out.persistence.mapper.UserPersistenceMapper;
import com.bookapp.backend.domain.model.follower.Follower;
import com.bookapp.backend.domain.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class FollowerPersistenceMapperTest {

    private UserPersistenceMapper userMapper;
    private FollowerPersistenceMapper mapper;

    @BeforeEach
    void setUp() {
        userMapper = mock(UserPersistenceMapper.class);
        mapper = new FollowerPersistenceMapper(userMapper);
    }

    @Test
    void toDomain_shouldMapEntityToDomain() {
        FollowerEntity entity = new FollowerEntity();
        UserEntity followerEntity = new UserEntity();
        UserEntity followedEntity = new UserEntity();

        followerEntity.setId(1L);
        followedEntity.setId(2L);

        entity.setFollower(followerEntity);
        entity.setFollowedPerson(followedEntity);

        User follower = new User(1L);
        User followed = new User(2L);

        when(userMapper.toDomain(followerEntity)).thenReturn(follower);
        when(userMapper.toDomain(followedEntity)).thenReturn(followed);

        Follower domain = mapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(1L, domain.getId()); // basiert auf followerId
        assertEquals(follower, domain.getFollower());
        assertEquals(followed, domain.getFollowed());
    }

    @Test
    void toEntity_shouldMapDomainToEntity() {
        User follower = new User(1L);
        User followed = new User(2L);
        Follower domain = new Follower(1L, follower, followed);

        UserEntity followerEntity = new UserEntity();
        UserEntity followedEntity = new UserEntity();

        when(userMapper.toEntity(follower)).thenReturn(followerEntity);
        when(userMapper.toEntity(followed)).thenReturn(followedEntity);

        FollowerEntity entity = mapper.toEntity(domain);

        assertNotNull(entity);
        assertEquals(followerEntity, entity.getFollower());
        assertEquals(followedEntity, entity.getFollowedPerson());
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
