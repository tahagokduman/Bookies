package com.bookapp.backend.adapter.in.web.dto.response.avatar;

import com.bookapp.backend.adapter.in.web.dto.response.common.BaseResponseDTO;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class AvatarResponseDto extends BaseResponseDTO<AvatarResponseDto> {
    private Long id;
    private String avatar;
}
