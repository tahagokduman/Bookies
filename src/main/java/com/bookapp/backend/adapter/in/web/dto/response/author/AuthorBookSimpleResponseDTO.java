package com.bookapp.backend.adapter.in.web.dto.response.author;

import com.bookapp.backend.adapter.in.web.dto.response.common.BaseResponseDTO;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class AuthorBookSimpleResponseDTO extends BaseResponseDTO<AuthorBookSimpleResponseDTO> {
    private Long authorId;
    private Long bookId;
}
