package com.bookapp.backend.adapter.in.web.dto.response.list;

import com.bookapp.backend.adapter.in.web.dto.response.book.BookResponseDTO;
import com.bookapp.backend.adapter.in.web.dto.response.book.BookShortResponseDTO;
import com.bookapp.backend.adapter.in.web.dto.response.common.BaseResponseDTO;
import com.bookapp.backend.adapter.in.web.dto.response.user.UserShortResponseDTO;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ListResponseDTO extends RepresentationModel<ListResponseDTO> {
    private Long id;
    private String title;
    private UserShortResponseDTO owner;
    private List<BookResponseDTO> books;
}
