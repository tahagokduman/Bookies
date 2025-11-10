package com.bookapp.PersistenceMapperTests;

import com.bookapp.backend.adapter.out.persistence.entity.BookEntity;
import com.bookapp.backend.adapter.out.persistence.mapper.BookPersistenceMapper;
import com.bookapp.backend.domain.model.book.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookPersistenceMapperTest {

    private BookPersistenceMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new BookPersistenceMapper();
    }

    @Test
    void testToDomain() {
        BookEntity entity = new BookEntity();
        entity.setId(1L);
        entity.setTitle("Test Title");
        entity.setIsbn("123456789");
        entity.setAuthor("Jane Doe");
        entity.setDescription("A description");
        entity.setCoverImageUrl("http://image.url");
        entity.setPageCount(200);
        entity.setPublishedYear(2020);
        entity.setPublisher("Test Publisher");
        entity.setGenre("Thriller");
        entity.setLanguage("en");

        Book book = mapper.toDomain(entity);

        assertNotNull(book);
        assertEquals(1L, book.getId());
        assertEquals("Test Title", book.getTitle());
        assertEquals("123456789", book.getIsbn());
        assertEquals("Jane Doe", book.getAuthor());
        assertEquals("A description", book.getDescription());
        assertEquals("http://image.url", book.getCoverImageUrl());
        assertEquals(200, book.getPageCount());
        assertEquals(2020, book.getPublishedYear());
        assertEquals("Test Publisher", book.getPublisher());
        assertEquals("Thriller", book.getGenre());
        assertEquals("en", book.getLanguage());
    }

    @Test
    void testToEntity() {
        Book book = new Book();
        book.setId(2L);
        book.setTitle("Domain Title");
        book.setIsbn("987654321");
        book.setAuthor("John Smith");
        book.setDescription("Another description");
        book.setCoverImageUrl("http://domain.url");
        book.setPageCount(300);
        book.setPublishedYear(2021);
        book.setPublisher("Domain Publisher");
        book.setGenre("Science Fiction");
        book.setLanguage("de");

        BookEntity entity = mapper.toEntity(book);

        assertNotNull(entity);
        assertEquals(2L, entity.getId());
        assertEquals("Domain Title", entity.getTitle());
        assertEquals("987654321", entity.getIsbn());
        assertEquals("John Smith", entity.getAuthor());
        assertEquals("Another description", entity.getDescription());
        assertEquals("http://domain.url", entity.getCoverImageUrl());
        assertEquals(300, entity.getPageCount());
        assertEquals(2021, entity.getPublishedYear());
        assertEquals("Domain Publisher", entity.getPublisher());
        assertEquals("Science Fiction", entity.getGenre());
        assertEquals("de", entity.getLanguage());
    }

    @Test
    void testToDomainWithNull() {
        assertNull(mapper.toDomain(null));
    }

    @Test
    void testToEntityWithNull() {
        assertNull(mapper.toEntity(null));
    }
}
