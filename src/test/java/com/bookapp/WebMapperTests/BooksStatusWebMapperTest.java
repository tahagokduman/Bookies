package com.bookapp.WebMapperTests;
import com.bookapp.backend.adapter.in.web.dto.request.status.BooksStatusRequestDTO;
import com.bookapp.backend.adapter.in.web.mapper.BooksStatusWebMapper;
import com.bookapp.backend.domain.model.book.Book;
import com.bookapp.backend.domain.model.status.BooksStatus;
import com.bookapp.backend.domain.model.status.Status;
import com.bookapp.backend.domain.model.user.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BooksStatusWebMapperTest {

    private final BooksStatusWebMapper mapper = new BooksStatusWebMapper();

    @Test
    void testToDomain() {
        BooksStatusRequestDTO dto = new BooksStatusRequestDTO();
        dto.setUserId(1L);
        dto.setBookId(2L);
        dto.setStatusId(3L);

        BooksStatus result = mapper.toDomain(dto);

        assertNotNull(result);
        assertEquals(1L, result.getUser().getId());
        assertEquals(2L, result.getBook().getId());
        assertEquals(3L, result.getStatus().getId());
    }

    @Test
    void testToResponseDto() {
        User user = new User(1L);
        Book book = new Book(2L);
        book.setTitle("1984");

        Status status = new Status(3L);
        status.setStatus("Gelesen");

        BooksStatus bs = new BooksStatus();
        bs.setId(100L);
        bs.setUser(user);
        bs.setBook(book);
        bs.setStatus(status);
        bs.setCreatedAt(null);
        bs.setUpdatedAt(null);

        var dto = mapper.toResponseDto(bs);

        assertNotNull(dto);
        assertEquals(2L, dto.getBook().getId());
        assertEquals("1984", dto.getBook().getTitle());
        assertEquals(3L, dto.getStatus().getId());
        assertEquals("Gelesen", dto.getStatus().getStatus());
    }
}
