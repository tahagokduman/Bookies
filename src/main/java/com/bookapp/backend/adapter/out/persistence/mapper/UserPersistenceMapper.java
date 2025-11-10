package com.bookapp.backend.adapter.out.persistence.mapper;

import com.bookapp.backend.adapter.out.persistence.entity.UserEntity;
import com.bookapp.backend.domain.model.base.NonNegativeInteger;
import com.bookapp.backend.domain.model.user.User;
import org.springframework.stereotype.Component;

@Component
public class UserPersistenceMapper {
    public UserEntity toEntity(User user) {
        if (user == null) return null;

        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setUsername(user.getUsername());
        entity.setEmail(user.getEmail());
        entity.setPassword(user.getPassword());
        entity.setBirthdayDate(user.getBirthdayDate());
        entity.setFollowersCount(
                user.getFollowersCount() != null ? user.getFollowersCount().getValue() : 0
        );
        entity.setFollowedPersonCount(
                user.getFollowingCount() != null ? user.getFollowingCount().getValue() : 0
        );
        return entity;
    }

    // Entity -> Domain
    public User toDomain(UserEntity entity) {
        if (entity == null) return null;

        return new User(
                entity.getId(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getBirthdayDate(),
                new NonNegativeInteger(entity.getFollowersCount()),
                new NonNegativeInteger(entity.getFollowedPersonCount())
        );
    }
}
