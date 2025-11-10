package com.bookapp.backend.adapter.in.web.dto.response.author;

import com.bookapp.backend.adapter.in.web.dto.response.common.BaseResponseDTO;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorResponseDTO extends BaseResponseDTO<AuthorResponseDTO> {
    private String name;
    private String description;
    private int bookCount;
}
