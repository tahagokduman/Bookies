package com.bookapp.WebMapperTests;
import com.bookapp.backend.adapter.in.web.dto.response.book.BookResponseDTO;
import com.bookapp.backend.adapter.in.web.mapper.BookWebMapper;
import com.bookapp.backend.domain.model.book.Book;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BookWebMapperTest {

    private final BookWebMapper mapper = new BookWebMapper();

    @Test
    void testToResponseDto() {
        Book book = new Book();
        book.setId(5L);
        book.setTitle("Book Title");
        book.setIsbn("3333333333333");
        book.setAuthor("Author Name");
        book.setDescription("Book description");
        book.setCoverImageUrl("http://image.jpg");
        book.setPageCount(200);
        book.setPublisher("Publisher A");
        book.setPublishedYear(2020);
        book.setGenre("Fantasy");
        book.setLanguage("German");
        book.setCreatedAt(LocalDateTime.now());
        book.setUpdatedAt(LocalDateTime.now());

        BookResponseDTO dto = mapper.toResponseDto(book);

        assertEquals(5L, dto.getId());
        assertEquals("Book Title", dto.getTitle());
        assertEquals("3333333333333", dto.getIsbn());
        assertEquals("Book description", dto.getDescription());
        assertEquals("http://image.jpg", dto.getCoverImageUrl());
        assertEquals(200, dto.getPageCount());
        assertEquals("Publisher A", dto.getPublisher());
        assertEquals(2020, dto.getPublishedYear());
        assertEquals("Fantasy", dto.getGenre());
        assertEquals("German", dto.getLanguage());
        assertNotNull(dto.getCreatedAt());
        assertNotNull(dto.getUpdatedAt());
        assertEquals("Author Name", dto.getAuthor().getName());
    }
}
