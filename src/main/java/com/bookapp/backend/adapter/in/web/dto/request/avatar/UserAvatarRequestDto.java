package com.bookapp.backend.adapter.in.web.dto.request.avatar;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAvatarRequestDto {
    private Long userId;
    private Long avatarId;
}
