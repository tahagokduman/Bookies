package com.bookapp.backend.adapter.in.web.dto.response.book;

import com.bookapp.backend.adapter.in.web.dto.response.common.BaseResponseDTO;
import com.bookapp.backend.adapter.in.web.dto.response.user.UserShortResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikedBookResponseDTO extends BaseResponseDTO<LikedBookResponseDTO> {
    private BookResponseDTO book;
    private UserShortResponseDTO user;
}

