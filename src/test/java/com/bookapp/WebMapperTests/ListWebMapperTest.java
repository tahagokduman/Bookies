package com.bookapp.WebMapperTests;

import com.bookapp.backend.adapter.in.web.dto.request.list.ListCreateRequestDTO;
import com.bookapp.backend.adapter.in.web.dto.response.book.BookResponseDTO;
import com.bookapp.backend.adapter.in.web.dto.response.list.ListResponseDTO;
import com.bookapp.backend.adapter.in.web.dto.response.list.ListShortResponseDTO;
import com.bookapp.backend.adapter.in.web.dto.response.user.UserShortResponseDTO;
import com.bookapp.backend.adapter.in.web.mapper.BookWebMapper;
import com.bookapp.backend.adapter.in.web.mapper.ListWebMapper;
import com.bookapp.backend.domain.model.book.Book;
import com.bookapp.backend.domain.model.list.List;
import com.bookapp.backend.domain.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class ListWebMapperTest {

    private ListWebMapper mapper;

    @BeforeEach
    void setup() {
        BookWebMapper bookWebMapper = new BookWebMapper();
        mapper = new ListWebMapper(bookWebMapper);
    }

    @Test
    void testToDomain() {
        ListCreateRequestDTO dto = new ListCreateRequestDTO();
        dto.setName("My List");
        dto.setUserId(1L);

        List result = mapper.toDomain(dto);

        assertEquals("My List", result.getName());
        assertNotNull(result.getUser());
        assertEquals(1L, result.getUser().getId());
    }

    @Test
    void testToShortDto() {
        List list = new List();
        list.setId(10L);
        list.setName("Reading");

        ListShortResponseDTO dto = mapper.toShortDto(list);

        assertEquals(10L, dto.getId());
        assertEquals("Reading", dto.getTitle());
    }

    @Test
    void testToResponseDto() {
        User user = new User();
        user.setId(2L);
        user.setUsername("max");

        Book book = new Book();
        book.setId(100L);
        book.setTitle("Test Book");
        book.setIsbn("1234567890");
        book.setCoverImageUrl("img.jpg");

        List list = new List();
        list.setId(5L);
        list.setName("Favorites");
        list.setUser(user);
        list.setBooksList(Collections.singletonList(book));

        ListResponseDTO dto = mapper.toResponseDto(list);

        assertEquals(5L, dto.getId());
        assertEquals("Favorites", dto.getTitle());

        UserShortResponseDTO owner = dto.getOwner();
        assertNotNull(owner);
        assertEquals(2L, owner.getId());
        assertEquals("max", owner.getUsername());

        assertNotNull(dto.getBooks());
        assertEquals(1, dto.getBooks().size());
        BookResponseDTO bookDto = dto.getBooks().get(0);
        assertEquals(100L, bookDto.getId());
        assertEquals("Test Book", bookDto.getTitle());
        assertEquals("1234567890", bookDto.getIsbn());
        assertEquals("img.jpg", bookDto.getCoverImageUrl());
    }
}
