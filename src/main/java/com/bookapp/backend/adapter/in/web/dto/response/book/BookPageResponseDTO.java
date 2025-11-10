package com.bookapp.backend.adapter.in.web.dto.response.book;

import com.bookapp.backend.adapter.in.web.dto.response.common.PageResponseDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class BookPageResponseDTO extends PageResponseDTO<BookShortResponseDTO> {

    public BookPageResponseDTO(List<BookShortResponseDTO> content, int pageNumber, int pageSize, long totalElements, int totalPages) {
        super(content, pageNumber, pageSize, totalElements, totalPages);
    }
}
