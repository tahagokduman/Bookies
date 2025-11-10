package com.bookapp.backend.adapter.out.persistence.mapper;

import com.bookapp.backend.adapter.out.persistence.entity.FollowerEntity;
import com.bookapp.backend.domain.model.follower.Follower;
import com.bookapp.backend.domain.model.id.FollowerId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FollowerPersistenceMapper {
    private final UserPersistenceMapper userMapper;

    public Follower toDomain(FollowerEntity entity) {
        if (entity == null) return null;
        return new Follower(
                entity.getFollower().getId(),
                userMapper.toDomain(entity.getFollower()),
                userMapper.toDomain(entity.getFollowedPerson())
        );
    }

    public FollowerEntity toEntity(Follower domain) {
        if (domain == null) return null;

        FollowerEntity entity = new FollowerEntity();

        FollowerId id = new FollowerId(
                domain.getFollower().getId(),
                domain.getFollowed().getId()
        );
        entity.setId(id);

        entity.setFollower(userMapper.toEntity(domain.getFollower()));
        entity.setFollowedPerson(userMapper.toEntity(domain.getFollowed()));
        return entity;
    }
}