package com.bookapp.WebMapperTests;
import com.bookapp.backend.adapter.in.web.dto.request.follow.LikedBookRequestDTO;
import com.bookapp.backend.adapter.in.web.dto.response.book.LikedBookResponseDTO;
import com.bookapp.backend.adapter.in.web.mapper.LikedBookWebMapper;
import com.bookapp.backend.domain.model.book.Book;
import com.bookapp.backend.domain.model.follower.LikedBook;
import com.bookapp.backend.domain.model.user.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class LikedBookWebMapperTest {

    private final LikedBookWebMapper mapper = new LikedBookWebMapper();

    @Test
    void toDomain_shouldMapCorrectly() {
        LikedBookRequestDTO dto = new LikedBookRequestDTO();
        dto.setUserId(1L);
        dto.setBookId(2L);

        LikedBook likedBook = mapper.toDomain(dto);

        assertNotNull(likedBook);
        assertEquals(1L, likedBook.getUser().getId());
        assertEquals(2L, likedBook.getBook().getId());
    }

    @Test
    void toResponseDto_shouldMapCorrectly() {
        User user = new User(1L);
        user.setUsername("testuser");

        Book book = new Book(2L);
        book.setTitle("Test Book");

        LikedBook likedBook = new LikedBook(3L, user, book);
        likedBook.setCreatedAt(LocalDateTime.now());
        likedBook.setUpdatedAt(LocalDateTime.now());

        LikedBookResponseDTO dto = mapper.toResponseDto(likedBook);

        assertNotNull(dto);
        assertEquals(3L, dto.getId());
        assertEquals(1L, dto.getUser().getId());
        assertEquals("testuser", dto.getUser().getUsername());
        assertEquals(2L, dto.getBook().getId());
        assertEquals("Test Book", dto.getBook().getTitle());
    }
}
