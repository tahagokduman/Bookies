package com.bookapp.backend.adapter.in.web.mapper;

import com.bookapp.backend.adapter.in.web.dto.request.list.BooksInListRequestDTO;
import com.bookapp.backend.adapter.in.web.dto.response.book.BookShortResponseDTO;
import com.bookapp.backend.adapter.in.web.dto.response.book.BooksInListResponseDTO;
import com.bookapp.backend.adapter.in.web.dto.response.list.ListShortResponseDTO;
import com.bookapp.backend.domain.model.book.Book;
import com.bookapp.backend.domain.model.list.BooksInList;
import com.bookapp.backend.domain.model.list.List;
import org.springframework.stereotype.Component;

@Component
public class BooksInListWebMapper {

    public BooksInList toDomain(BooksInListRequestDTO dto) {
        BooksInList entity = new BooksInList();
        entity.setList(new List(dto.getListId()));
        entity.setBook(new Book(dto.getBookId()));
        return entity;
    }

    public BooksInListResponseDTO toResponseDto(BooksInList entity) {
        BooksInListResponseDTO dto = new BooksInListResponseDTO();

        dto.setId(entity.getId());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        BookShortResponseDTO bookDto = new BookShortResponseDTO();
        bookDto.setId(entity.getBook().getId());
        bookDto.setTitle(entity.getBook().getTitle());
        dto.setBook(bookDto);

        ListShortResponseDTO listDto = new ListShortResponseDTO();
        listDto.setId(entity.getList().getId());
        listDto.setTitle(entity.getList().getName());
        dto.setList(listDto);

        return dto;
    }
}
