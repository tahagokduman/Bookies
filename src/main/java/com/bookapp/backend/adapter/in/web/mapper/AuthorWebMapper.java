package com.bookapp.backend.adapter.in.web.mapper;


import com.bookapp.backend.adapter.in.web.dto.request.author.AuthorCreateRequestDTO;
import com.bookapp.backend.adapter.in.web.dto.request.author.AuthorUpdateRequestDTO;
import com.bookapp.backend.adapter.in.web.dto.response.author.AuthorResponseDTO;
import com.bookapp.backend.domain.model.book.Author;
import org.springframework.stereotype.Component;

@Component
public class AuthorWebMapper {

    public Author toDomain(AuthorCreateRequestDTO dto) {
        Author author = new Author();
        author.setName(dto.getName());
        author.setDescription(dto.getDescription());
        return author;
    }

    public Author toDomain(AuthorUpdateRequestDTO dto) {
        Author author = new Author();
        author.setId(dto.getId());
        author.setName(dto.getName());
        author.setDescription(dto.getDescription());
        return author;
    }

    public AuthorResponseDTO toResponseDto(Author author) {
        AuthorResponseDTO dto = new AuthorResponseDTO();
        dto.setId(author.getId());
        dto.setCreatedAt(author.getCreatedAt());
        dto.setUpdatedAt(author.getUpdatedAt());
        dto.setName(author.getName());
        dto.setDescription(author.getDescription());
        dto.setBookCount(author.getBooks().size());
        return dto;
    }
}
