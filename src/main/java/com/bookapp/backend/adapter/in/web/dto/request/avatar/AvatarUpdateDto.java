package com.bookapp.backend.adapter.in.web.dto.request.avatar;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class AvatarUpdateDto {
    private Long avatarId;
    private String avatar;
}
