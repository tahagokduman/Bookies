package com.bookapp.PersistenceMapperTests;

import com.bookapp.backend.adapter.out.persistence.entity.BooksInListEntity;
import com.bookapp.backend.adapter.out.persistence.entity.BookEntity;
import com.bookapp.backend.adapter.out.persistence.entity.ListEntity;
import com.bookapp.backend.adapter.out.persistence.mapper.BookPersistenceMapper;
import com.bookapp.backend.adapter.out.persistence.mapper.BooksInListPersistenceMapper;
import com.bookapp.backend.adapter.out.persistence.mapper.ListPersistenceMapper;
import com.bookapp.backend.domain.model.book.Book;
import com.bookapp.backend.domain.model.id.BooksInListId;
import com.bookapp.backend.domain.model.list.BooksInList;
import com.bookapp.backend.domain.model.list.List;
import com.bookapp.backend.domain.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class BooksInListPersistenceMapperTest {

    private BookPersistenceMapper bookMapper;
    private ListPersistenceMapper listMapper;
    private BooksInListPersistenceMapper mapper;

    @BeforeEach
    void setup() {
        bookMapper = mock(BookPersistenceMapper.class);
        listMapper = mock(ListPersistenceMapper.class);
        mapper = new BooksInListPersistenceMapper(bookMapper, listMapper);
    }

    @Test
    void testToDomain() {
        BookEntity bookEntity = new BookEntity();
        bookEntity.setId(1L);

        ListEntity listEntity = new ListEntity();
        listEntity.setId(2L);

        BooksInListEntity entity = new BooksInListEntity();

        BooksInListId id = new BooksInListId();
        entity.setId(id);

        entity.setBook(bookEntity);
        entity.setList(listEntity);

        Book book = new Book(
                "Title",
                "ISBN12345",
                "author",
                "A great book",
                "url-to-cover-image",
                300,
                "Publisher Name",
                2022,
                "Fantasy",
                "de"
        );
        book.setId(1L);

        List list = new List(
                2L,
                new User("username", "john.doe@gmail.com", "******", LocalDate.of(2003, 5, 26)),
                "List Name"
        );

        when(bookMapper.toDomain(bookEntity)).thenReturn(book);
        when(listMapper.toDomain(listEntity)).thenReturn(list);

        BooksInList domain = mapper.toDomain(entity);

        assertNotNull(domain);

        assertEquals(book, domain.getBook());
        assertEquals(list, domain.getList());
    }

    @Test
    void testToEntity() {
        Book book = new Book(
                "Title",
                "ISBN12345",
                "author",
                "A great book",
                "url-to-cover-image",
                300,
                "Publisher Name",
                2022,
                "Krimi",
                "Deutsch"
        );
        book.setId(3L);

        List list = new List(
                4L,
                new User("username", "john.doe@gmail.com", "******", LocalDate.of(2003, 5, 26)),
                "List Name"
        );

        BooksInList domain = new BooksInList();
        domain.setBook(book);
        domain.setList(list);
        domain.setId(999L);

        BookEntity bookEntity = new BookEntity();
        bookEntity.setId(3L);

        ListEntity listEntity = new ListEntity();
        listEntity.setId(4L);

        when(bookMapper.toEntity(book)).thenReturn(bookEntity);
        when(listMapper.toEntity(list)).thenReturn(listEntity);

        BooksInListEntity entity = mapper.toEntity(domain);

        assertNotNull(entity);
        assertEquals(bookEntity, entity.getBook());
        assertEquals(listEntity, entity.getList());
        assertNotNull(entity.getId());
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
