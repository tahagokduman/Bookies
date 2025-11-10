package com.bookapp.WebMapperTests;

import com.bookapp.backend.adapter.in.web.dto.request.author.AuthorBookRequestDTO;
import com.bookapp.backend.adapter.in.web.dto.response.author.AuthorBookSimpleResponseDTO;
import com.bookapp.backend.adapter.in.web.mapper.AuthorBookWebMapper;
import com.bookapp.backend.domain.model.book.Author;
import com.bookapp.backend.domain.model.book.AuthorBook;
import com.bookapp.backend.domain.model.book.Book;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AuthorBookWebMapperTest {

    private final AuthorBookWebMapper mapper = new AuthorBookWebMapper();

    @Test
    void testToDomain() {
        AuthorBookRequestDTO dto = new AuthorBookRequestDTO();
        dto.setAuthorId(1L);
        dto.setBookId(2L);

        AuthorBook domain = mapper.toDomain(dto);

        assertNotNull(domain);
        assertEquals(1L, domain.getAuthor().getId());
        assertEquals(2L, domain.getBook().getId());
    }

    @Test
    void testToResponseDto() {
        Author author = new Author(1L);
        Book book = new Book(2L);
        AuthorBook entity = new AuthorBook();
        entity.setId(10L);
        entity.setAuthor(author);
        entity.setBook(book);
        entity.setCreatedAt(LocalDateTime.now().minusDays(1));
        entity.setUpdatedAt(LocalDateTime.now());

        AuthorBookSimpleResponseDTO dto = mapper.toResponseDto(entity);

        assertNotNull(dto);
        assertEquals(10L, dto.getId());
        assertEquals(1L, dto.getAuthorId());
        assertEquals(2L, dto.getBookId());
        assertEquals(entity.getCreatedAt(), dto.getCreatedAt());
        assertEquals(entity.getUpdatedAt(), dto.getUpdatedAt());
    }
}
