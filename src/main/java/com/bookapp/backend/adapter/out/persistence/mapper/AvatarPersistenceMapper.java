package com.bookapp.backend.adapter.out.persistence.mapper;

import com.bookapp.backend.adapter.out.persistence.entity.AvatarEntity;
import com.bookapp.backend.domain.model.user.Avatar;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AvatarPersistenceMapper {

    public AvatarEntity toEntity(Avatar avatar){
        AvatarEntity entity = new AvatarEntity();
        entity.setId(avatar.getId());
        entity.setAvatar(avatar.getAvatar());
        return entity;
    }
    public Avatar toDomain(AvatarEntity entity){

        Avatar avatar = new Avatar();
        avatar.setId(entity.getId());
        avatar.setAvatar(entity.getAvatar());

        return avatar;
    }

}
