package com.bookapp.backend.adapter.in.web.mapper;

import com.bookapp.backend.adapter.in.web.dto.request.avatar.AvatarUpdateDto;
import com.bookapp.backend.adapter.in.web.dto.response.avatar.AvatarResponseDto;
import com.bookapp.backend.domain.model.user.Avatar;
import org.springframework.stereotype.Component;

@Component
public class AvatarWebMapper {

    public Avatar toDomain(AvatarUpdateDto dto) {
        Avatar avatar = new Avatar();
        avatar.setAvatar(dto.getAvatar());
        avatar.setAvatar(dto.getAvatar());
        avatar.setId(dto.getAvatarId());
        return avatar;
    }

    public AvatarResponseDto toResponseDto(Avatar avatar) {
        AvatarResponseDto dto = new AvatarResponseDto();
        dto.setAvatar(avatar.getAvatar());
        dto.setId(avatar.getId());
        avatar.setAvatar(avatar.getAvatar());
        return dto;
    }
}

