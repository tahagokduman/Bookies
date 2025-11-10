package com.bookapp.backend.adapter.in.web.mapper;

import com.bookapp.backend.adapter.in.web.dto.request.list.ListCreateRequestDTO;
import com.bookapp.backend.adapter.in.web.dto.response.book.BookResponseDTO;
import com.bookapp.backend.adapter.in.web.dto.response.book.BookShortResponseDTO;
import com.bookapp.backend.adapter.in.web.dto.response.list.ListResponseDTO;
import com.bookapp.backend.adapter.in.web.dto.response.list.ListShortResponseDTO;
import com.bookapp.backend.adapter.in.web.dto.response.user.UserShortResponseDTO;
import com.bookapp.backend.domain.model.book.Book;
import com.bookapp.backend.domain.model.follower.LikedBook;
import com.bookapp.backend.domain.model.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import com.bookapp.backend.domain.model.list.List;


@Component
@RequiredArgsConstructor
public class ListWebMapper {

    private final BookWebMapper bookWebMapper;

    public List toDomain(ListCreateRequestDTO dto) {
        List list = new List();
        list.setUser(new User(dto.getUserId()));
        list.setName(dto.getName());
        return list;
    }

    public ListShortResponseDTO toShortDto(List list) {
        ListShortResponseDTO dto = new ListShortResponseDTO();
        dto.setId(list.getId());
        dto.setTitle(list.getName());
        return dto;
    }

    public ListResponseDTO toResponseDto(List entity) {
        ListResponseDTO dto = new ListResponseDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getName());

        UserShortResponseDTO owner = new UserShortResponseDTO();
        owner.setId(entity.getUser().getId());
        owner.setUsername(entity.getUser().getUsername());
        dto.setOwner(owner);

        java.util.List<BookResponseDTO> bookShortResponseDTOList =
                entity.getBooksList().stream().map(bookWebMapper::toResponseDto).toList();
        dto.setBooks(bookShortResponseDTOList);

        return dto;
    }
}
