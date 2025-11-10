package com.bookapp.WebMapperTests;

import com.bookapp.backend.adapter.in.web.dto.request.list.BooksInListRequestDTO;
import com.bookapp.backend.adapter.in.web.dto.response.book.BooksInListResponseDTO;
import com.bookapp.backend.adapter.in.web.mapper.BooksInListWebMapper;
import com.bookapp.backend.domain.model.book.Book;
import com.bookapp.backend.domain.model.list.BooksInList;
import com.bookapp.backend.domain.model.list.List;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BooksInListWebMapperTest {

    private final BooksInListWebMapper mapper = new BooksInListWebMapper();

    @Test
    void testToDomain() {
        BooksInListRequestDTO dto = new BooksInListRequestDTO();
        dto.setListId(10L);
        dto.setBookId(20L);

        BooksInList entity = mapper.toDomain(dto);

        assertNotNull(entity);
        assertEquals(10L, entity.getList().getId());
        assertEquals(20L, entity.getBook().getId());
    }

    @Test
    void testToResponseDto() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Testbuch");

        List list = new List();
        list.setId(2L);
        list.setName("Meine Liste");

        BooksInList entity = new BooksInList();
        entity.setId(100L);
        entity.setCreatedAt(LocalDateTime.now().minusDays(2));
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setBook(book);
        entity.setList(list);
        entity.setOrderInList(null);

        BooksInListResponseDTO dto = mapper.toResponseDto(entity);

        assertNotNull(dto);
        assertEquals(100L, dto.getId());

        assertNotNull(dto.getBook());
        assertEquals("Testbuch", dto.getBook().getTitle());
        assertEquals(1L, dto.getBook().getId());

        assertNotNull(dto.getList());
        assertEquals("Meine Liste", dto.getList().getTitle());
        assertEquals(2L, dto.getList().getId());

        assertNull(dto.getOrderInList()); // ← geändert
        assertEquals(entity.getCreatedAt(), dto.getCreatedAt());
        assertEquals(entity.getUpdatedAt(), dto.getUpdatedAt());
    }
}
