package com.bookapp.backend.adapter.in.web.dto.response.avatar;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAvatarResponseDto extends RepresentationModel<UserAvatarResponseDto> {
    private Long userId;
    private Long avatarId;
    private String avatar;
}
