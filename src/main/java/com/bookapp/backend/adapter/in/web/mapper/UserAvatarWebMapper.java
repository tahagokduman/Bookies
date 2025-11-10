package com.bookapp.backend.adapter.in.web.mapper;

import com.bookapp.backend.adapter.in.web.dto.request.avatar.UserAvatarRequestDto;
import com.bookapp.backend.adapter.in.web.dto.response.avatar.UserAvatarResponseDto;
import com.bookapp.backend.domain.model.id.UserAvatarId;
import com.bookapp.backend.domain.model.user.Avatar;
import com.bookapp.backend.domain.model.user.User;
import com.bookapp.backend.domain.model.user.UserAvatar;
import org.springframework.stereotype.Component;

@Component
public class UserAvatarWebMapper {
    public UserAvatar toDomain(UserAvatarRequestDto dto){
        UserAvatar model = new UserAvatar();
        model.setId(new UserAvatarId(dto.getUserId(), dto.getAvatarId()));
        model.setAvatar(new Avatar(dto.getAvatarId()));
        model.setUser(new User(dto.getUserId()));

        return model;
    }

    public UserAvatarResponseDto toResponseDto(UserAvatar model){
        UserAvatarResponseDto dto = new UserAvatarResponseDto();
        dto.setAvatarId(model.getAvatar().getId());
        dto.setAvatar(model.getAvatar().getAvatar());
        dto.setUserId(model.getUser().getId());

        return dto;
    }

}
