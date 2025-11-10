package com.bookapp.backend.adapter.out.persistence.mapper;

import com.bookapp.backend.adapter.out.persistence.entity.UserAvatarEntity;
import com.bookapp.backend.domain.model.user.UserAvatar;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserAvatarPersistenceMapper {

    private final AvatarPersistenceMapper avatarMapper;
    private final UserPersistenceMapper userMapper;

    public UserAvatarEntity toEntity(UserAvatar userAvatar){
        UserAvatarEntity entity = new UserAvatarEntity();
        entity.setAvatar(avatarMapper.toEntity(userAvatar.getAvatar()));
        entity.setUser(userMapper.toEntity(userAvatar.getUser()));
        entity.setId(userAvatar.getId());
        return entity;
    }

    public UserAvatar toDomain(UserAvatarEntity entity){
        UserAvatar userAvatar = new UserAvatar();
        userAvatar.setAvatar(avatarMapper.toDomain(entity.getAvatar()));
        userAvatar.setUser(userMapper.toDomain(entity.getUser()));
        userAvatar.setId(entity.getId());

        return userAvatar;
    }
}
