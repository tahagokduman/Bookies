package com.bookapp.backend.adapter.in.web.dto.response.book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookShortResponseDTO {
    private Long id;
    private String title;
    private String isbn;
    private String coverImageUrl;
}
