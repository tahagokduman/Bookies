package com.bookapp.WebMapperTests;

import com.bookapp.backend.adapter.in.web.dto.request.author.AuthorCreateRequestDTO;
import com.bookapp.backend.adapter.in.web.dto.request.author.AuthorUpdateRequestDTO;
import com.bookapp.backend.adapter.in.web.dto.response.author.AuthorResponseDTO;
import com.bookapp.backend.adapter.in.web.mapper.AuthorWebMapper;
import com.bookapp.backend.domain.model.book.Author;
import com.bookapp.backend.domain.model.book.Book;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AuthorWebMapperTest {

    private final AuthorWebMapper mapper = new AuthorWebMapper();

    @Test
    void testToDomainFromCreateDTO() {
        AuthorCreateRequestDTO dto = new AuthorCreateRequestDTO();
        dto.setName("Max Mustermann");
        dto.setDescription("Ein sehr berühmter Autor");

        Author author = mapper.toDomain(dto);

        assertNotNull(author);
        assertEquals("Max Mustermann", author.getName());
        assertEquals("Ein sehr berühmter Autor", author.getDescription());
    }

    @Test
    void testToDomainFromUpdateDTO() {
        AuthorUpdateRequestDTO dto = new AuthorUpdateRequestDTO();
        dto.setId(1L);
        dto.setName("Erika Musterfrau");
        dto.setDescription("Neue Beschreibung");

        Author author = mapper.toDomain(dto);

        assertNotNull(author);
        assertEquals(1L, author.getId());
        assertEquals("Erika Musterfrau", author.getName());
        assertEquals("Neue Beschreibung", author.getDescription());
    }

    @Test
    void testToResponseDto() {
        Author author = new Author();
        author.setId(5L);
        author.setName("Test Autor");
        author.setDescription("Test Beschreibung");
        author.setBooks(List.of(new Book(1L), new Book(2L)));
        author.setCreatedAt(LocalDateTime.now().minusDays(1));
        author.setUpdatedAt(LocalDateTime.now());

        AuthorResponseDTO dto = mapper.toResponseDto(author);

        assertNotNull(dto);
        assertEquals(5L, dto.getId());
        assertEquals("Test Autor", dto.getName());
        assertEquals("Test Beschreibung", dto.getDescription());
        assertEquals(2, dto.getBookCount());
        assertEquals(author.getCreatedAt(), dto.getCreatedAt());
        assertEquals(author.getUpdatedAt(), dto.getUpdatedAt());
    }
}
