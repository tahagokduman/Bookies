package com.bookapp.backend.adapter.in.web.dto.response.book;

import com.bookapp.backend.adapter.in.web.dto.response.author.AuthorResponseDTO;
import com.bookapp.backend.adapter.in.web.dto.response.common.BaseResponseDTO;
import com.bookapp.backend.adapter.in.web.dto.response.list.ListShortResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BooksInListResponseDTO extends BaseResponseDTO<BooksInListResponseDTO> {
    private BookShortResponseDTO book;
    private ListShortResponseDTO list;
    private Integer orderInList;
}
