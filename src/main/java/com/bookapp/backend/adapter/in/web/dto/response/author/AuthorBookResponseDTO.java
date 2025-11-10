package com.bookapp.backend.adapter.in.web.dto.response.author;

import com.bookapp.backend.adapter.in.web.dto.response.book.BookShortResponseDTO;
import com.bookapp.backend.adapter.in.web.dto.response.common.BaseResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorBookResponseDTO extends BaseResponseDTO<AuthorBookResponseDTO> {
    private Long authorId;
    private String authorName;
    private List<BookShortResponseDTO> books;
}

