package com.bookapp.backend.adapter.in.web.dto.response.user;

import com.bookapp.backend.adapter.in.web.dto.response.common.BaseResponseDTO;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterResponseDTO extends BaseResponseDTO<UserRegisterResponseDTO> {
    private Long id;
    private String username;
    private String message;
}
