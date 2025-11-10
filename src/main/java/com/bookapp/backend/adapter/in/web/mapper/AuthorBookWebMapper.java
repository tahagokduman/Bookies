package com.bookapp.backend.adapter.in.web.mapper;

import com.bookapp.backend.adapter.in.web.dto.request.author.AuthorBookRequestDTO;
import com.bookapp.backend.adapter.in.web.dto.response.author.AuthorBookSimpleResponseDTO;
import com.bookapp.backend.domain.model.book.Author;
import com.bookapp.backend.domain.model.book.AuthorBook;
import com.bookapp.backend.domain.model.book.Book;
import org.springframework.stereotype.Component;

@Component
public class AuthorBookWebMapper {

    public AuthorBook toDomain(AuthorBookRequestDTO dto) {
        AuthorBook entity = new AuthorBook();
        entity.setAuthor(new Author(dto.getAuthorId()));
        entity.setBook(new Book(dto.getBookId()));
        return entity;
    }

    public AuthorBookSimpleResponseDTO toResponseDto(AuthorBook entity) {
        AuthorBookSimpleResponseDTO dto = new AuthorBookSimpleResponseDTO();
        dto.setId(entity.getId());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setAuthorId(entity.getAuthor().getId());
        dto.setBookId(entity.getBook().getId());
        return dto;
    }

}
